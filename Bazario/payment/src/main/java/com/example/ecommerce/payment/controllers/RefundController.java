package com.example.ecommerce.payment.controllers;

import com.example.ecommerce.payment.services.RefundService;
import com.example.ecommerce.payment.models.Payment;
import com.example.ecommerce.payment.models.Refund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refunds")
public class RefundController {

    @Autowired
    private RefundService refundService;

    @PostMapping
    public Refund refundPayment(@RequestBody Payment payment) {
        return refundService.processRefund(payment);
    }
}
