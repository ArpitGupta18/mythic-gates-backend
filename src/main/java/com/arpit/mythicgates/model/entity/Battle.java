package com.arpit.mythicgates.model.entity;

import com.arpit.mythicgates.model.enums.BattleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "battles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Battle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_character_id", nullable = false)
    private UserCharacter userCharacter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "boss_id", nullable = false)
    private Boss boss;

    @Column(name = "player_current_health", nullable = false)
    private Integer playerCurrentHealth;

    @Column(name = "player_current_mana", nullable = false)
    private Integer playerCurrentMana;

    @Column(name = "boss_current_health", nullable = false)
    private Integer bossCurrentHealth;

    @Column(name = "turn_count", nullable = false)
    private Integer turnCount;

    @Column(name = "damage_dealt", nullable = false)
    private Integer damageDealt;

    @Column(name = "gold_earned", nullable = false)
    private Integer goldEarned;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BattleStatus status;

    @Builder.Default
    @OneToMany(
            mappedBy = "battle",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BattleSkillCooldown> skillCooldowns = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
