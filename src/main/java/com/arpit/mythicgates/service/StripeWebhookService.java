package com.arpit.mythicgates.service;

import com.stripe.exception.SignatureVerificationException;

public interface StripeWebhookService {
    void handleEvent(
            String payload,
            String sigHeader,
            String webhookSecret
    ) throws SignatureVerificationException;
}
