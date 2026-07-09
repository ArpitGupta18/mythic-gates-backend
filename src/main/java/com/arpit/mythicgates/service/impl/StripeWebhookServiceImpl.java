package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.model.entity.PaymentTransaction;
import com.arpit.mythicgates.model.entity.User;
import com.arpit.mythicgates.model.enums.PaymentStatus;
import com.arpit.mythicgates.repository.PaymentTransactionRepository;
import com.arpit.mythicgates.repository.UserRepository;
import com.arpit.mythicgates.service.StripeWebhookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripeWebhookServiceImpl implements StripeWebhookService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void handleEvent(String payload, String sigHeader, String webhookSecret) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(
                payload,
                sigHeader,
                webhookSecret
        );

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event
                    .getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow();

            completePayment(session);
        }
    }

    private void completePayment(Session session) {
        PaymentTransaction transaction = paymentTransactionRepository
                .findByStripeSessionId(session.getId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() == PaymentStatus.COMPLETED) {
            return;
        }

        User user = transaction.getUser();

        user.setGold(user.getGold() + transaction.getGoldAmount());

        transaction.setStatus(PaymentStatus.COMPLETED);

        userRepository.save(user);
        paymentTransactionRepository.save(transaction);
    }
}
