package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.ResourceNotFoundException;
import com.arpit.mythicgates.helper.UserHelper;
import com.arpit.mythicgates.model.dto.payment.ProductRequest;
import com.arpit.mythicgates.model.dto.payment.StripeResponse;
import com.arpit.mythicgates.model.entity.GoldPackage;
import com.arpit.mythicgates.model.entity.PaymentTransaction;
import com.arpit.mythicgates.model.entity.User;
import com.arpit.mythicgates.model.enums.PaymentStatus;
import com.arpit.mythicgates.repository.GoldPackageRepository;
import com.arpit.mythicgates.repository.PaymentTransactionRepository;
import com.arpit.mythicgates.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("stripeService")
public class StripeServiceImpl implements PaymentService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    @Value("${stripe.secretKey}")
    private String secretKey;

    private final UserHelper userHelper;
    private final GoldPackageRepository goldPackageRepository;

    @Override
    public StripeResponse checkoutProducts(ProductRequest productRequest) {
        User user = userHelper.getCurrentUser();

        GoldPackage goldPackage = goldPackageRepository.
                findByPublicIdAndActiveTrue(productRequest.packageId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Gold package not found"));

        Stripe.apiKey = secretKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(goldPackage.getName())
                .build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(goldPackage.getCurrency())
                .setUnitAmount(goldPackage.getPriceAmount().longValue())
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(priceData)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/game/dashboard?payment=success")
                .setCancelUrl("http://localhost:4200/game/recharge?payment=cancelled")
                .putMetadata("userId", user.getId().toString())
                .putMetadata("packageId", goldPackage.getId().toString())
                .addLineItem(lineItem)
                .build();

        Session session = null;

        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        PaymentTransaction transaction = PaymentTransaction.builder()
                .user(user)
                .goldPackage(goldPackage)
                .stripeSessionId(session.getId())
                .amount(goldPackage.getPriceAmount())
                .currency(goldPackage.getCurrency())
                .goldAmount(goldPackage.getGoldAmount())
                .status(PaymentStatus.PENDING)
                .build();

        paymentTransactionRepository.save(transaction);

        return new StripeResponse(
                "SUCCESS",
                "Payment session created ",
                session.getId(),
                session.getUrl()
        );
    }
}
