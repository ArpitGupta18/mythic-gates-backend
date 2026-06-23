package com.arpit.mythicgates.repository;

import com.arpit.mythicgates.model.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {
    boolean existsByUserIdAndCharacterId(Long user, Long character);

    List<UserCharacter> findByUserId(Long userId);

    Optional<UserCharacter> findByUserIdAndCharacterPublicId(
            Long userId,
            UUID characterId
    );
}
