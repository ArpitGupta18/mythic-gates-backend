package com.arpit.mythicgates.model.dto.boss;

import com.arpit.mythicgates.model.enums.Rarity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BossResponse(
        UUID publicId,
        String name,
        Rarity rarity,
        Integer baseHealth,
        Integer attack,
        Integer defense,
        BigDecimal critChance,
        BigDecimal dodgeChance,
        BigDecimal rageThreshold,
        Integer rewardMin,
        Integer rewardMax,
        Integer healInterval,
        BigDecimal healPercentage,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
