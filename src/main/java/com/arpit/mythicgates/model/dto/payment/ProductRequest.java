package com.arpit.mythicgates.model.dto.payment;

public record ProductRequest(
        Long amount,
        Long quantity,
        String name,
        String currency
) {
}
