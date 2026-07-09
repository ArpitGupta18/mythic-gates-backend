package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.payment.ProductRequest;
import com.arpit.mythicgates.model.dto.payment.StripeResponse;

public interface PaymentService {
    StripeResponse checkoutProducts(ProductRequest productRequest);
}
