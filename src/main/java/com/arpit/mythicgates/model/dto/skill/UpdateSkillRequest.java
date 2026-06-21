package com.arpit.mythicgates.model.dto.skill;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateSkillRequest(
        @NotBlank(message = "Skill name is required")
        String name,

        @NotNull(message = "Damage multiplier is required")
        @DecimalMin(value = "0.01", message = "Damage multiplier must be greater than 0")
        BigDecimal damageMultiplier,

        @NotNull(message = "Mana cost is required")
        @Min(value = 0, message = "Mana cost cannot be negative")
        Integer manaCost
) {
}
