package com.arpit.mythicgates.model.dto.battle;

import com.arpit.mythicgates.model.enums.BattleStatus;

public record AttackBattleResponse(
        BattleResponse battle,
        String playerActionMessage,
        String bossActionMessage,
        boolean battleEnded,
        BattleStatus result
) {
}
