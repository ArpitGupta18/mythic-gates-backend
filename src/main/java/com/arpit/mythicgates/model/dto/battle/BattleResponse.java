package com.arpit.mythicgates.model.dto.battle;

import com.arpit.mythicgates.model.enums.BattleStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record BattleResponse(
        UUID battleId,
        String characterName,
        String bossName,
        Integer playerCurrentHealth,
        Integer playerCurrentMana,
        Integer bossCurrentHealth,
        Integer turnCount,
        Integer damageDealt,
        Integer goldEarned,
        BattleStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
