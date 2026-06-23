package com.arpit.mythicgates.model.dto.battle;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AttackBattleRequest(
        @NotNull(message = "Skill is required")
        UUID skillId
) {
}
