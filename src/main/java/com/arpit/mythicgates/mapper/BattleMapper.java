package com.arpit.mythicgates.mapper;

import com.arpit.mythicgates.model.dto.battle.BattleResponse;
import com.arpit.mythicgates.model.dto.battle.SkillCooldownResponse;
import com.arpit.mythicgates.model.entity.Battle;

import java.util.List;

public class BattleMapper {
    public static BattleResponse toBattleResponseDto(Battle battle) {
        List<SkillCooldownResponse> skillCooldowns = battle.getSkillCooldowns()
                .stream()
                .map(cooldown -> new SkillCooldownResponse(
                        cooldown.getSkill().getPublicId(),
                        cooldown.getSkill().getName(),
                        cooldown.getSkill().getCooldown(),
                        cooldown.getRemainingCooldown()
                ))
                .toList();

        return new BattleResponse(
                battle.getPublicId(),
                battle.getUserCharacter().getCharacter().getPublicId(),
                battle.getUserCharacter().getCharacter().getName(),
                battle.getUserCharacter().getCharacter().getImageUrl(),
                battle.getBoss().getPublicId(),
                battle.getBoss().getName(),
                battle.getBoss().getImageUrl(),
                battle.getUserCharacter().getCharacter().getBaseHealth(),
                battle.getPlayerCurrentHealth(),
                battle.getUserCharacter().getCharacter().getBaseMana(),
                battle.getPlayerCurrentMana(),
                battle.getBoss().getBaseHealth(),
                battle.getBossCurrentHealth(),
                battle.getTurnCount(),
                battle.getDamageDealt(),
                battle.getGoldEarned(),
                battle.getStatus(),
                battle.getCreatedAt(),
                battle.getUpdatedAt(),
                skillCooldowns
        );
    }
}
