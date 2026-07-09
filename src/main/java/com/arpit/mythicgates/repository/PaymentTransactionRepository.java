package com.arpit.mythicgates.repository;

import com.arpit.mythicgates.model.entity.PaymentTransaction;
import com.arpit.mythicgates.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findByStripeSessionId(String stripeSessionId);
    boolean existsByStripeSessionIdAndStatus(
            String stripeSessionId,
            PaymentStatus status
    );
}
