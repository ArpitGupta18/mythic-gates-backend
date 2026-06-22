package com.arpit.mythicgates.repository;

import com.arpit.mythicgates.model.entity.Boss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BossRepository extends JpaRepository<Boss, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<Boss> findByPublicId(UUID bossId);
}
