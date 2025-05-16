package com.example.Paymentgp.controller;

import com.example.Paymentgp.model.Payment;
import com.example.Paymentgp.model.DiscountCode;
import com.example.Paymentgp.service.PaymentService;
import com.example.Paymentgp.strategy.PaymentStrategy;
import com.example.Paymentgp.strategy.CreditCardPayment;
import com.example.Paymentgp.strategy.PayPalPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
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
