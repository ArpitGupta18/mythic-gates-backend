package com.arpit.mythicgates.repository;

import com.arpit.mythicgates.model.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findByPublicId(UUID publicId);
    boolean existsByNameIgnoreCase(String name);
    List<Character> findByIsStarterTrue();
}
