package com.arpit.mythicgates.model.dto.battle;

import com.arpit.mythicgates.model.enums.BattleStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BattleResponse(
        UUID battleId,
        UUID characterId,
        String characterName,
        String characterImageUrl,
        UUID bossId,
        String bossName,
        String bossImageUrl,
        Integer playerMaxHealth,
        Integer playerCurrentHealth,
        Integer playerMaxMana,
        Integer playerCurrentMana,
        Integer bossMaxHealth,
        Integer bossCurrentHealth,
        Integer turnCount,
        Integer damageDealt,
        Integer goldEarned,
        BattleStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<SkillCooldownResponse> skillCooldowns
) {
}
