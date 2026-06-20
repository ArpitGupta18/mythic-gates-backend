package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.model.dto.auth.RegisterRequest;
import com.arpit.mythicgates.model.dto.auth.RegisterResponse;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
