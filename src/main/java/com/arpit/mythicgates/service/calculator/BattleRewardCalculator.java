package com.arpit.mythicgates.service.calculator;

import com.arpit.mythicgates.model.entity.Boss;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class BattleRewardCalculator {

    public int calculateGoldEarned(
            Boss boss,
            BigDecimal damagePercentage,
            boolean bossDefeated
    ) {
        int baseReward = bossDefeated
                ? calculateRandomVictoryReward(boss)
                : RewardConstants.PARTICIPATION_REWARD;

        int damageBonus = calculateDamageBonus(boss, damagePercentage);

        int defeatBonus = bossDefeated ? RewardConstants.BOSS_DEFEATED : 0;

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
            return (int) (rewardRange * 1.10);
        }

        if (damagePercentage.compareTo(BigDecimal.valueOf(50)) >= 0) {
            return (int) (rewardRange * 0.75);
        }

        if (damagePercentage.compareTo(BigDecimal.valueOf(25)) >= 0) {
            return (int) (rewardRange * 0.35);
        }

        return (int) (rewardRange * 0.15);
    }
}
