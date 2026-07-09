package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.model.dto.payment.ProductRequest;
import com.arpit.mythicgates.model.dto.payment.StripeResponse;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class PaymentController {
    private final @Qualifier("stripeService") PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<StripeResponse>> checkoutProducts(
            @RequestBody ProductRequest productRequest
    ) {
        StripeResponse stripeResponse = paymentService.checkoutProducts(productRequest);

        return ApiResponseUtil.success("Payment Success", stripeResponse);
    }
}
