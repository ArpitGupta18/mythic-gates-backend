package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.model.dto.payment.ProductRequest;
import com.arpit.mythicgates.model.dto.payment.StripeResponse;
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
    @Value("${stripe.secretKey}")
    private String secretKey;

    @Override
    public StripeResponse checkoutProducts(ProductRequest productRequest) {
        Stripe.apiKey = secretKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(productRequest.name())
                .build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(productRequest.currency() != null ? productRequest.currency() : "USD")
                .setUnitAmount(productRequest.amount())
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(productRequest.quantity())
                .setPriceData(priceData)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/game/dashboard")
                .setCancelUrl("http://localhost:4200/game/recharge")
                .addLineItem(lineItem)
                .build();

        Session session = null;

        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        return new StripeResponse(
                "SUCCESS",
                "Payment session created ",
                session.getId(),
                session.getUrl()
        );
    }
}
