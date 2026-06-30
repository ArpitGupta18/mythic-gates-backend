package com.arpit.mythicgates.mapper;

import com.arpit.mythicgates.model.dto.skill.SkillResponse;
import com.arpit.mythicgates.model.entity.Skill;

public class SkillMapper {
    public static SkillResponse toSkillResponseDto(Skill entity) {
        if (entity == null)
            return null;

        return new SkillResponse(
                entity.getPublicId(),
                entity.getName(),
                entity.getType(),
                entity.getSlot(),
                entity.getDamageMultiplier(),
                entity.getManaCost(),
                entity.getCooldown()
        );
    }
}
