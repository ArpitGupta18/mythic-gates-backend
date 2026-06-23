package com.arpit.mythicgates.repository;

import com.arpit.mythicgates.model.entity.Battle;
import com.arpit.mythicgates.model.enums.BattleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BattleRepository extends JpaRepository<Battle, Long> {
    Optional<Battle> findByPublicId(UUID publicId);

    Optional<Battle> findByUserCharacterUserIdAndStatus(
            Long userId,
            BattleStatus status
    );

    List<Battle> findByUserCharacterUserIdOrderByCreatedAtDesc(Long userId);
}
