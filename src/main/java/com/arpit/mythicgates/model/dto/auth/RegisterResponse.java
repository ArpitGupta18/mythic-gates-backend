package com.arpit.mythicgates.model.dto.auth;

import com.arpit.mythicgates.model.enums.Role;

public record RegisterResponse(
    String username,
    String email,
    Role role,
    Integer gold,
    Integer bossesDefeated
) {

}
