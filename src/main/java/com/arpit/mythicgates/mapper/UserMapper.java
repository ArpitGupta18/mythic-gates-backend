package com.arpit.mythicgates.mapper;

import com.arpit.mythicgates.model.dto.user.UserProfileResponse;
import com.arpit.mythicgates.model.entity.User;

public class UserMapper {
    public static UserProfileResponse toUserProfileResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserProfileResponse(
                user.getPublicId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getGold(),
                user.getBossesDefeated()
        );
    }
}
