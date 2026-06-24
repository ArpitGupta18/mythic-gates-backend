package com.arpit.mythicgates.model.dto.battle;

import com.arpit.mythicgates.model.enums.BattleStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record BattleResponse(
        UUID battleId,
        String characterName,
        String bossName,
        Integer playerMaxHealth,
        Integer playerCurrentHealth,
        Integer playerMaxMana,
        Integer playerCurrentMana,
        Integer bossMaxHeatlh,
        Integer bossCurrentHealth,
        Integer turnCount,
        Integer damageDealt,
        Integer goldEarned,
        BattleStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
