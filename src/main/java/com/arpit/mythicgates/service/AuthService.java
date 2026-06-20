package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.auth.*;
import com.arpit.mythicgates.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse<RegisterResponse>> register(RegisterRequest request);

    ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request);

    ResponseEntity<ApiResponse<LoginResponse>> refreshToken(RefreshTokenRequest request);
}
