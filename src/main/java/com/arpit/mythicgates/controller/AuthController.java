package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.UnauthorizedException;
import com.arpit.mythicgates.model.dto.auth.*;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UnauthorizedException("Refresh token missing");
        }
        return authService.refreshToken(refreshToken);
    }


    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return authService.logout();
    }
}
