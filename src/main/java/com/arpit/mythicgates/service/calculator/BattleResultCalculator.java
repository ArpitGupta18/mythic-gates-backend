package com.arpit.mythicgates.service.calculator;

import com.arpit.mythicgates.model.entity.Battle;
import com.arpit.mythicgates.model.entity.User;
import com.arpit.mythicgates.model.enums.BattleStatus;
import com.arpit.mythicgates.service.EconomyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class BattleResultCalculator {
    private final BattleRewardCalculator battleRewardCalculator;
    private final EconomyService economyService;
//    private final UserService userService;

    public void endBattleAsWon(Battle battle, User user) {
        battle.setStatus(BattleStatus.WON);

        int goldEarned = battleRewardCalculator.calculateGoldEarned(
                battle.getBoss(),
                BigDecimal.valueOf(100),
                true
        );

        battle.setGoldEarned(goldEarned);

        economyService.addGold(user.getUsername(), goldEarned);
//        userService.incrementBossDefeated(user.getId());
    }

    public void endBattleAsLost(Battle battle, User user) {
        battle.setStatus(BattleStatus.LOST);

        BigDecimal damagePercentage = BigDecimal.valueOf(battle.getDamageDealt())
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        BigDecimal.valueOf(battle.getBoss().getBaseHealth()),
                        2,
                        RoundingMode.HALF_UP
                );

        int goldEarned = battleRewardCalculator.calculateGoldEarned(
                battle.getBoss(),
                damagePercentage,
                false
        );

        battle.setGoldEarned(goldEarned);

        economyService.addGold(user.getUsername(), goldEarned);
    }
}
