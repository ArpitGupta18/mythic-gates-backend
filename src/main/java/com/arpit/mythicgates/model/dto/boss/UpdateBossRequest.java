package com.arpit.mythicgates.model.dto.boss;

import com.arpit.mythicgates.model.enums.Rarity;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateBossRequest(
        @NotBlank(message = "Boss name is required")
        @Size(max = 100, message = "Boss name cannot exceed 100 characters")
        String name,

        @NotNull(message = "Rarity is required")
        Rarity rarity,

        @NotNull(message = "Base health is required")
        @Positive(message = "Base health must be greater than 0")
        Integer baseHealth,

        @NotNull(message = "Attack is required")
        @Positive(message = "Attack must be greater than 0")
        Integer attack,

        @NotNull(message = "Defense is required")
        @PositiveOrZero(message = "Defense cannot be negative")
        Integer defense,

        @NotNull(message = "Crit chance is required")
        @DecimalMin(value = "0.0", message = "Crit chance cannot be less than 0")
        @DecimalMax(value = "100.0", message = "Crit chance cannot exceed 100")
        BigDecimal critChance,

        @NotNull(message = "Dodge chance is required")
        @DecimalMin(value = "0.0", message = "Dodge chance cannot be less than 0")
        @DecimalMax(value = "100.0", message = "Dodge chance cannot exceed 100")
        BigDecimal dodgeChance,

        @NotNull(message = "Rage Threshold is required")
        @DecimalMin(value = "0.0", message = "Rage Threshold cannot be less than 0")
        @DecimalMax(value = "100.0", message = "Rage Threshold cannot exceed 100")
        BigDecimal rageThreshold,

        @NotNull(message = "Reward minimum is required")
        @PositiveOrZero(message = "Reward minimum cannot be negative")
        Integer rewardMin,

        @NotNull(message = "Reward maximum is required")
        @PositiveOrZero(message = "Reward maximum cannot be negative")
        Integer rewardMax,

        @NotNull(message = "Heal interval is required")
        @Positive(message = "Heal interval must be greater than 0")
        Integer healInterval,

        @NotNull(message = "Heal percentage is required")
        @DecimalMin(value = "0.0", message = "Heal percentage cannot be less than 0")
        @DecimalMax(value = "100.0", message = "Heal percentage cannot exceed 100")
        BigDecimal healPercentage
) {
}
