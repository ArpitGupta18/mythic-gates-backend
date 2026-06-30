package com.arpit.mythicgates.repository;

import com.arpit.mythicgates.model.entity.BattleSkillCooldown;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BattleSkillCooldownRepository extends JpaRepository<BattleSkillCooldown, Long> {
    Optional<BattleSkillCooldown> findByBattleIdAndSkillId(Long battleId, Long skillId);

    List<BattleSkillCooldown> findByBattleId(Long battleId);
}
