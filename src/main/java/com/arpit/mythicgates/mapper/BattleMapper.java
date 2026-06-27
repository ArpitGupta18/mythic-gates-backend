package com.arpit.mythicgates.mapper;

import com.arpit.mythicgates.model.dto.battle.BattleResponse;
import com.arpit.mythicgates.model.entity.Battle;

public class BattleMapper {
    public static BattleResponse toBattleResponseDto(Battle battle) {
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
                battle.getUpdatedAt()
        );
    }
}
