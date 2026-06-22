package com.arpit.mythicgates.repository;

import com.arpit.mythicgates.model.entity.Boss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BossRepository extends JpaRepository<Boss, Long> {
    boolean existsByNameIgnoreCase(String name);
}
