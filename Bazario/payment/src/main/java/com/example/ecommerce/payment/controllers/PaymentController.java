package com.example.ecommerce.payment.controllers;

import com.example.ecommerce.payment.services.PaymentService;
import com.example.ecommerce.payment.strategy.CreditCardPayment;
import com.example.ecommerce.payment.strategy.PayPalPayment;
import com.example.ecommerce.payment.strategy.PaymentStrategy;
import com.example.ecommerce.payment.models.Payment;
import com.example.ecommerce.payment.models.DiscountCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public String makePayment(@RequestBody Payment payment) {
        DiscountCode discountCode = DiscountCode.fromString(payment.getDiscountCode());
        payment.setDiscountAmount(discountCode.getDiscountAmount());
        PaymentStrategy paymentStrategy = determinePaymentMethod(payment);
        //Place holder for place order
        return paymentService.processPayment(payment, paymentStrategy);
    }

    private PaymentStrategy determinePaymentMethod(Payment payment) {
        if ("CREDIT_CARD".equalsIgnoreCase(payment.getPaymentMethod())) {
            return new CreditCardPayment();
        } else if ("PAYPAL".equalsIgnoreCase(payment.getPaymentMethod())) {
            return new PayPalPayment();
        } else {
            throw new IllegalArgumentException("Invalid payment method");
        }
    }
}
