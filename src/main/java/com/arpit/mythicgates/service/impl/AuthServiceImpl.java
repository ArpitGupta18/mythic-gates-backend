package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.UserAlreadyExistsException;
import com.arpit.mythicgates.mapper.AuthMapper;
import com.arpit.mythicgates.model.dto.auth.RegisterRequest;
import com.arpit.mythicgates.model.dto.auth.RegisterResponse;
import com.arpit.mythicgates.model.entity.User;
import com.arpit.mythicgates.model.enums.Role;
import com.arpit.mythicgates.repository.UserRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.AuthService;
import com.arpit.mythicgates.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<ApiResponse<RegisterResponse>> register(RegisterRequest request) {
        String username = request.username().trim();
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new UserAlreadyExistsException("User already exists with this username");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User already exists with this email");
        }

        Integer gold = 0;

        User user = AuthMapper.toEntity(request);

        user.setPublicId(UuidGenerator.generator());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);
        user.setGold(gold);
        user.setBossesDefeated(0);

        User savedUser = userRepository.save(user);

        RegisterResponse response = AuthMapper.toRegisterResponseDto(savedUser);
        return ApiResponseUtil.created("User registered successfully", response);
    }
}
