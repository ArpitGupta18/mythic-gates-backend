package com.arpit.mythicgates.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "battle_skill_cooldowns",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_battle_skill_cooldown",
                        columnNames = {"battle_id", "skill_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BattleSkillCooldown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "battle_id", nullable = false)
    private Battle battle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "remaining_cooldown", nullable = false)
    private Integer remainingCooldown;
}
