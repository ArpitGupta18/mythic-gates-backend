package com.arpit.mythicgates.mapper;

import com.arpit.mythicgates.model.dto.auth.RegisterRequest;
import com.arpit.mythicgates.model.dto.auth.RegisterResponse;
import com.arpit.mythicgates.model.entity.User;

public class AuthMapper {
    public static User toEntity(RegisterRequest request) {
        if (request == null) {
            return null;
        }

        return User.builder()
                .username(request.username())
                .email(request.email())
                .build();
    }

    public static RegisterResponse toRegisterResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return new RegisterResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getGold(),
                user.getBossesDefeated()
        );
    }
}
