package com.identity_service.controller;

import com.identity_service.dto.request.AuthRequest;
import com.identity_service.dto.response.ApiResponse;
import com.identity_service.dto.response.AuthResponse;
import com.identity_service.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        boolean result = authService.authenticate(authRequest);
        return ApiResponse.<AuthResponse>builder()
                .result(AuthResponse.builder()
                        .authenticated(result)
                        .build())
                .build();
    }
}
