package com.example.payment.controller;

import com.example.payment.model.Payment;
import com.example.payment.model.Refund;
import com.example.payment.service.RefundService;
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
