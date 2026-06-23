package com.arpit.mythicgates.service.calculator;

import com.arpit.mythicgates.model.dto.battle.DamageResult;
import com.arpit.mythicgates.model.entity.Battle;
import com.arpit.mythicgates.model.entity.Boss;
import com.arpit.mythicgates.model.entity.Character;
import com.arpit.mythicgates.model.entity.Skill;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class BattleAttackCalculator {
    public DamageResult calculatePlayerDamage(
            Character character,
            Boss boss,
            Skill skill
    ) {
        boolean dodged = isDodged(boss.getDodgeChance());

        if (dodged) {
            return new DamageResult(0, false, true);
        }

        int rawDamage = skill.getDamageMultiplier()
                .multiply(BigDecimal.valueOf(character.getAttack()))
                .intValue();

        int finalDamage = rawDamage - boss.getDefense();

        finalDamage = Math.max(1, finalDamage);
        boolean critical = isCriticalHit(character.getCritChance());

        if (critical) {
            finalDamage *= 2;
        }

        return new DamageResult(
                Math.max(1, finalDamage),
                critical,
                false
        );
    }

    public DamageResult calculateBossDamage(
            Boss boss,
            Character character,
            Battle battle
    ) {
        boolean dodged = isDodged(character.getDodgeChance());

        if (dodged) {
            return new DamageResult(0, false, true);
        }

        int damage = boss.getAttack() - character.getDefense();

        damage = Math.max(1, damage);

        BigDecimal bossHealthPercentage = BigDecimal.valueOf(battle.getBossCurrentHealth())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(boss.getBaseHealth()), 2, RoundingMode.HALF_UP);

        if (bossHealthPercentage.compareTo(boss.getRageThreshold()) <= 0) {
            damage = (int) Math.ceil(damage * 1.3);
        }

        boolean critical = isCriticalHit(boss.getCritChance());

        if (critical) {
            damage *= 2;
        }

        return new DamageResult(Math.max(1, damage), critical, false);
    }

    public String healBossIfNeeded(Battle battle, Boss boss) {
        if (battle.getTurnCount() % boss.getHealInterval() != 0) {
            return "";
        }

        if (battle.getBossCurrentHealth() >= boss.getBaseHealth()) {
            return "";
        }

        int healAmount = BigDecimal.valueOf(boss.getBaseHealth())
                .multiply(boss.getHealPercentage())
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP)
                .intValue();

        int oldHealth = battle.getBossCurrentHealth();

        int newHealth = Math.min(
                boss.getBaseHealth(),
                oldHealth + healAmount
        );

        battle.setBossCurrentHealth(newHealth);

        int actualHealed = newHealth - oldHealth;

        return boss.getName() + " healed for " + actualHealed + " HP.";
    }

    private boolean isCriticalHit(BigDecimal critChance) {
        double roll = ThreadLocalRandom.current().nextDouble(0, 100);

        return BigDecimal.valueOf(roll).compareTo(critChance) <= 0;
    }

    private boolean isDodged(BigDecimal dodgeChance) {
        double roll = ThreadLocalRandom.current().nextDouble(0, 100);

        return BigDecimal.valueOf(roll).compareTo(dodgeChance) <= 0;
    }
}
