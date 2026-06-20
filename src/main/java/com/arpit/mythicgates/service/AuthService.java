package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.auth.RegisterRequest;
import com.arpit.mythicgates.model.dto.auth.RegisterResponse;
import com.arpit.mythicgates.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse<RegisterResponse>> register(RegisterRequest request);
}
