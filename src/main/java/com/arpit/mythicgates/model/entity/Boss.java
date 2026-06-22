package com.arpit.mythicgates.model.entity;

import com.arpit.mythicgates.model.enums.Rarity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bosses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Boss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rarity rarity;

    @Column(name = "base_health", nullable = false)
    private Integer baseHealth;

    @Column(nullable = false)
    private Integer attack;

    @Column(nullable = false)
    private Integer defense;

    @Column(name = "crit_chance", nullable = false, precision = 5, scale = 2)
    private BigDecimal critChance;

    @Column(name = "dodge_chance", nullable = false, precision = 5, scale = 2)
    private BigDecimal dodgeChance;

    @Column(name = "rage_threshold", nullable = false, precision = 5, scale = 2)
    private BigDecimal rageThreshold;

    @Column(name = "reward_min", nullable = false)
    private Integer rewardMin;

    @Column(name = "reward_max", nullable = false)
    private Integer rewardMax;

    @Column(name = "heal_interval", nullable = false)
    private Integer healInterval;

    @Column(name = "heal_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal healPercentage;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

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
