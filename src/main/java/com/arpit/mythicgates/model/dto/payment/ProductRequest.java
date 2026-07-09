package com.arpit.mythicgates.model.dto.payment;

import java.util.UUID;

public record ProductRequest(
        UUID packageId,
        Long amount,
        Long quantity,
        String name,
        String currency
) {
}
