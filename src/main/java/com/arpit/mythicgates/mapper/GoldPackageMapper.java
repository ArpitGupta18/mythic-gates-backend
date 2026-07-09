package com.arpit.mythicgates.mapper;

import com.arpit.mythicgates.model.dto.gold.GoldPackageResponse;
import com.arpit.mythicgates.model.entity.GoldPackage;

public class GoldPackageMapper {
    public static GoldPackageResponse toGoldPackageResponse(GoldPackage entity) {
        if (entity == null)
            return null;

        return new GoldPackageResponse(
                entity.getPublicId(),
                entity.getName(),
                entity.getGoldAmount(),
                entity.getPriceAmount(),
                entity.getCurrency()
        );
    }
}
