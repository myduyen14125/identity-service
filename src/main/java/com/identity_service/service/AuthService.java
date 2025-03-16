package com.identity_service.service;

import com.identity_service.dto.request.AuthRequest;
import com.identity_service.dto.request.IntrospectRequest;
import com.identity_service.dto.response.AuthResponse;
import com.identity_service.dto.response.IntrospectResponse;
import com.identity_service.entity.InvalidatedToken;
import com.identity_service.entity.User;
import com.identity_service.exception.AppException;
import com.identity_service.exception.ErrorCode;
import com.identity_service.repository.InvalidatedTokenRepository;
import com.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    UserRepository userRepository;
    InvalidatedTokenRepository tokenRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal // make non final so that it will not be injected to constructor
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthResponse authenticate(AuthRequest request) {
        val user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Match user's password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isAuthenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        val token = generateToken(user);

        return AuthResponse.builder()
                .accessToken(token)
                .authenticated(true)
                .build();
    }

    public void logout(String token) throws ParseException, JOSEException {
        val signedToken = verifyToken(token);
        String jti = signedToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = signedToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expirationTime(expirationTime)
                .build();

        tokenRepository.save(invalidatedToken);
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("identity-service.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can not create token" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        val token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private SignedJWT verifyToken (String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        val verified = signedJWT.verify(verifier);
        if (!(verified && expirationDate.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }
    // build scope is String contains Role user from Entity
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" "); // by the standard of scope in JWT -> mush separate by " "
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                        stringJoiner.add("ROLE_" + role.getName());
                        if (!CollectionUtils.isEmpty(role.getPermissions())) {
                            role.getPermissions().forEach(permission -> {
                                stringJoiner.add(permission.getName());
                            });
                        }
                    }
                    );
        };
        return stringJoiner.toString();
    }
}
