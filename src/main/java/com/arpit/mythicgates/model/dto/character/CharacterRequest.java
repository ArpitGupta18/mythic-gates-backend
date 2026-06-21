package com.arpit.mythicgates.model.dto.character;

import com.arpit.mythicgates.model.enums.Rarity;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CharacterRequest(
        @NotBlank(message = "Character name is required")
        @Size(max = 100, message = "Character name cannot exceed 100 characters")
        String name,

        @NotNull(message = "Rarity is required")
        Rarity rarity,

        @NotNull(message = "Base health is required")
        @Positive(message = "Base health must be greater than 0")
        Integer baseHealth,

        @NotNull(message = "Base mana is required")
        @Positive(message = "Base mana must be greater than 0")
        Integer baseMana,

        @NotNull(message = "Attack is required")
        @Positive(message = "Attack must be greater than 0")
        Integer attack,

        @NotNull(message = "Defense is required")
        @PositiveOrZero(message = "Defense cannot be negative")
        Integer defense,

        @NotNull(message = "Heal power is required")
        @PositiveOrZero(message = "Heal power cannot be negative")
        Integer healPower,

        @NotNull(message = "Crit chance is required")
        @DecimalMin(value = "0.0", message = "Crit chance cannot be less than 0")
        @DecimalMax(value = "100.0", message = "Crit chance cannot exceed 100")
        BigDecimal critChance,

        @NotNull(message = "Dodge chance is required")
        @DecimalMin(value = "0.0", message = "Dodge chance cannot be less than 0")
        @DecimalMax(value = "100.0", message = "Dodge chance cannot exceed 100")
        BigDecimal dodgeChance,

        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price cannot be negative")
        Integer price
) {
}
