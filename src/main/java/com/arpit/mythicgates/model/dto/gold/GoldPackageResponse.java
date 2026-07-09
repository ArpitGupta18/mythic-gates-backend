package com.arpit.mythicgates.model.dto.gold;

import java.util.UUID;

public record GoldPackageResponse(
        UUID packageId,
        String name,
        Integer goldAmount,
        Long priceAmount,
        String currency
) {
}
