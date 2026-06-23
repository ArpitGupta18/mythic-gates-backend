package com.arpit.mythicgates.service.calculator;

import com.arpit.mythicgates.model.entity.Battle;
import com.arpit.mythicgates.model.entity.Boss;
import com.arpit.mythicgates.model.entity.Character;
import com.arpit.mythicgates.model.entity.Skill;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class BattleAttackCalculator {
    public int calculatePlayerDamage(
            Character character,
            Boss boss,
            Skill skill
    ) {
        int rawDamage = skill.getDamageMultiplier()
                .multiply(BigDecimal.valueOf(character.getAttack()))
                .intValue();

        int finalDamage = rawDamage - boss.getDefense();

        return Math.max(1, finalDamage);
    }

    public int calculateBossDamage(
            Boss boss,
            Character character,
            Battle battle
    ) {
        int damage = boss.getAttack() - character.getDefense();

        damage = Math.max(1, damage);

        BigDecimal bossHealthPercentage = BigDecimal.valueOf(battle.getBossCurrentHealth())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(boss.getBaseHealth()), 2, RoundingMode.HALF_UP);

        if (bossHealthPercentage.compareTo(boss.getRageThreshold()) <= 0) {
            damage = (int) Math.ceil(damage * 1.3);
        }

        return damage;
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
}
