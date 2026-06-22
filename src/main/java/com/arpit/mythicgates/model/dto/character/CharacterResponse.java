package com.arpit.mythicgates.model.dto.character;

import com.arpit.mythicgates.model.enums.Rarity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CharacterResponse(
        UUID publicId,
        String name,
        Rarity rarity,
        Integer baseHealth,
        Integer baseMana,
        Integer attack,
        Integer defense,
        Integer healPower,
        BigDecimal critChance,
        BigDecimal dodgeChance,
        Integer price,
        String imageUrl,
        Boolean isStarter,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
