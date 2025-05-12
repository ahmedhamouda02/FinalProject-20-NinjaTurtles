package com.example.Paymentgp.controller;

import com.example.Paymentgp.model.Payment;
import com.example.Paymentgp.model.Refund;
import com.example.Paymentgp.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    @Autowired
    private RefundService refundService;

    @PostMapping
    public Refund refundPayment(@RequestBody Payment payment) {
        return refundService.processRefund(payment);
    }
}
