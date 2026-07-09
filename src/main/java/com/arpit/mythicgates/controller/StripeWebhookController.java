package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.service.StripeWebhookService;
import com.stripe.exception.SignatureVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {
    private final StripeWebhookService stripeWebhookService;

    @Value("${stripe.webhookSecret}")
    private String webhookSecret;

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) throws SignatureVerificationException {

        stripeWebhookService.handleEvent(payload, sigHeader, webhookSecret);

        return ResponseEntity.ok("success");
    }
}
