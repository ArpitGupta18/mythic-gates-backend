package com.arpit.mythicgates.mapper;

import com.arpit.mythicgates.model.dto.boss.BossResponse;
import com.arpit.mythicgates.model.entity.Boss;

public class BossMapper {
    public static BossResponse toBossResponseDto(Boss entity) {
        if (entity == null)
            return null;

        return new BossResponse(
                entity.getPublicId(),
                entity.getName(),
                entity.getRarity(),
                entity.getBaseHealth(),
                entity.getAttack(),
                entity.getDefense(),
                entity.getCritChance(),
                entity.getDodgeChance(),
                entity.getRageThreshold(),
                entity.getRewardMin(),
                entity.getRewardMax(),
                entity.getHealInterval(),
                entity.getHealPercentage(),
                entity.getImageUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
