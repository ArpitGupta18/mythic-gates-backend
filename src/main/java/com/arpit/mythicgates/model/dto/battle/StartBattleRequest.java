package com.arpit.mythicgates.model.dto.battle;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StartBattleRequest(
        @NotNull(message = "Character id is required")
        UUID characterId,

        @NotNull(message = "Boss id is required")
        UUID bossId
) {
}
