package com.arpit.mythicgates.service.calculator;

import com.arpit.mythicgates.model.entity.Boss;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class BattleRewardCalculator {
    private static final int PARTICIPATION_REWARD = 10;
    private static final int DEFEAT_BONUS = 20;

    public int calculateGoldEarned(
            Boss boss,
            BigDecimal damagePercentage,
            boolean bossDefeated
    ) {
        int baseReward = bossDefeated
                ? calculateRandomVictoryReward(boss)
                : PARTICIPATION_REWARD;

        int damageBonus = calculateDamageBonus(boss, damagePercentage);

        int defeatBonus = bossDefeated ? DEFEAT_BONUS : 0;

        return baseReward + damageBonus + defeatBonus;
    }

    private int calculateRandomVictoryReward(Boss boss) {
        return ThreadLocalRandom.current().nextInt(
                boss.getRewardMin(),
                boss.getRewardMax() + 1
        );
    }

    private int calculateDamageBonus(Boss boss, BigDecimal damagePercentage) {

        int rewardRange = boss.getRewardMax() - boss.getRewardMin();

        if (damagePercentage.compareTo(BigDecimal.valueOf(75)) >= 0) {
            return (int) (rewardRange * 0.50);
        }

        if (damagePercentage.compareTo(BigDecimal.valueOf(50)) >= 0) {
            return (int) (rewardRange * 0.30);
        }

        if (damagePercentage.compareTo(BigDecimal.valueOf(25)) >= 0) {
            return (int) (rewardRange * 0.15);
        }

        return 0;
    }
}
