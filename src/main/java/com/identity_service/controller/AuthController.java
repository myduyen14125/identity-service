package com.identity_service.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.identity_service.dto.request.AuthRequest;
import com.identity_service.dto.request.IntrospectRequest;
import com.identity_service.dto.request.LogoutRequest;
import com.identity_service.dto.request.RefreshRequest;
import com.identity_service.dto.response.ApiResponse;
import com.identity_service.dto.response.AuthResponse;
import com.identity_service.dto.response.IntrospectResponse;
import com.identity_service.service.AuthService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/token")
    ApiResponse<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.authenticate(authRequest);
        return ApiResponse.<AuthResponse>builder().result(authResponse).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authService.logout(logoutRequest.getToken());
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthResponse> refresh(@RequestBody RefreshRequest refreshRequest) throws ParseException, JOSEException {
        AuthResponse authResponse = authService.refreshToken(refreshRequest);
        return ApiResponse.<AuthResponse>builder().result(authResponse).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        IntrospectResponse response = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(response).build();
    }
}
