package com.example.ecommerce.payment.controllers;

import com.example.ecommerce.payment.DTO.PaymentsDTO;
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
    public String makePayment(@RequestBody PaymentsDTO paymentDTO) {
        return paymentService.processPayment(paymentDTO);
    }


}
