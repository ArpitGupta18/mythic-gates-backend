package com.arpit.mythicgates.model.dto.skill;

import com.arpit.mythicgates.model.enums.SkillType;

import java.math.BigDecimal;
import java.util.UUID;

public record SkillResponse(
        UUID publicId,
        String name,
        SkillType type,
        Integer slot,
        BigDecimal damageMultiplier,
        Integer manaCost,
        Integer cooldown
) {
}
