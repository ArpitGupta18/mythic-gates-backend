package com.arpit.mythicgates.model.dto.skill;

import com.arpit.mythicgates.model.enums.SkillType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record SkillRequest(
        @NotBlank(message = "Skill name is required")
        String name,

        @NotNull(message = "Skill type is required")
        SkillType type,

        @NotNull(message = "Skill slot is required")
        @Min(value = 1, message = "Slot must be between 1 and 2")
        @Max(value = 2, message = "Slot must be between 1 and 2")
        Integer slot,

        @NotNull(message = "Damage multiplier is required")
        @DecimalMin(value = "0.01", message = "Damage multiplier must be greater than 0")
        BigDecimal damageMultiplier,

        @NotNull(message = "Mana cost is required")
        @Min(value = 0, message = "Mana cost cannot be negative")
        Integer manaCost
) {
}
