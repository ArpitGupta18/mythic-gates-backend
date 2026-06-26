package com.arpit.mythicgates.model.dto.user;

import com.arpit.mythicgates.model.enums.Role;

import java.util.UUID;

public record UserProfileResponse(
        UUID publicId,
        String username,
        String email,
        Role role,
        Integer gold,
        Integer bossesDefeated
) {
}
