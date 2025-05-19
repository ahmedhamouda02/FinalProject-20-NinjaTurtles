package com.example.ecommerce.payment.controllers;

import com.example.ecommerce.payment.services.RefundService;
import com.example.ecommerce.payment.models.Payment;
import com.example.ecommerce.payment.models.Refund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/refunds")
public class RefundController {

    @Autowired
    private RefundService refundService;

    @PostMapping
    public Refund refundPayment(@RequestBody Payment payment) {
        return refundService.processRefund(payment);
    }

    @GetMapping
    public List<Refund> getAllRefunds() {
        return refundService.getAllRefunds();
    }

    @GetMapping("/points")
    public Integer getTotalPoints(
            @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
            @RequestParam(value = "userId", required = false) Long pathUserId) {
        Long userId = resolveUserId(headerUserId, pathUserId);
        return refundService.getTotalPointsByUserId(userId);
    }

    private Long resolveUserId(Long headerUserId, Long pathUserId) {
        if (headerUserId != null) {
            return headerUserId;
        }
        if (pathUserId != null) {
            return pathUserId;
        }
        throw new IllegalArgumentException("Missing userId (either in X-User-Id header or request parameter)");
    }
}
