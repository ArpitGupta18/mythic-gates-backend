package com.arpit.mythicgates.repository;

import com.arpit.mythicgates.model.entity.Skill;
import com.arpit.mythicgates.model.enums.SkillType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByPublicId(UUID skillId);

    boolean existsByCharacterPublicIdAndTypeAndSlot(UUID characterId, SkillType type, Integer slot);

    boolean existsByCharacterPublicId(UUID characterId);

    List<Skill> findByCharacterPublicIdOrderByTypeAscSlotAsc(UUID characterId);

    Optional<Skill> findByPublicIdAndCharacterId(UUID skillId, Long characterId);
}
