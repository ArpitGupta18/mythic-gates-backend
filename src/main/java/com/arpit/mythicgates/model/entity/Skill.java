package com.arpit.mythicgates.model.entity;

import com.arpit.mythicgates.model.enums.SkillType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "skills",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_character_skill_slot",
                        columnNames = {
                                "character_id",
                                "type",
                                "slot"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillType type;

    @Column(nullable = false)
    private Integer slot;

    @Column(name = "damage_multiplier", nullable = false, precision = 5, scale = 2)
    private BigDecimal damageMultiplier;

    @Column(name = "mana_cost", nullable = false)
    private Integer manaCost;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Column(nullable = false)
    private Integer cooldown = 0;

    @Builder.Default
    @OneToMany(
            mappedBy = "skill",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BattleSkillCooldown> battleSkillCooldowns = new ArrayList<>();

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
