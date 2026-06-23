package com.arpit.mythicgates.mapper;

import com.arpit.mythicgates.model.dto.battle.BattleResponse;
import com.arpit.mythicgates.model.entity.Battle;

public class BattleMapper {
    public static BattleResponse toBattleResponseDto(Battle battle) {
        return new BattleResponse(
                battle.getPublicId(),
                battle.getUserCharacter().getCharacter().getName(),
                battle.getBoss().getName(),
                battle.getPlayerCurrentHealth(),
                battle.getPlayerCurrentMana(),
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
