package com.arpit.mythicgates.model.dto.payment;

public record StripeResponse(
        String status,
        String message,
        String sessionId,
        String sessionUrl
) {
}
