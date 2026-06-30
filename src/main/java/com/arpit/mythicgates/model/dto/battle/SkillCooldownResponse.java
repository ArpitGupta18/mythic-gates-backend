package com.arpit.mythicgates.model.dto.battle;

import java.util.UUID;

public record SkillCooldownResponse(
        UUID skillId,
        String skillName,
        Integer cooldown,
        Integer remainingCooldown
) {
}
