package com.example.Paymentgp.service;

import com.example.Paymentgp.model.Payment;
import com.example.Paymentgp.model.Refund;
import com.example.Paymentgp.repository.RefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundService {

    @Autowired
    private RefundRepository refundRepository;
    public Refund processRefund(Payment payment) {
        Refund refund = new Refund();
        refund.setPoints(calculatePoints(payment.getAmount()));
        return refundRepository.save(refund);
    }

    private Integer calculatePoints(Double amount) {
        return (int) (amount / 10); // Example: 1 point for every 10 units
    }

    public RefundRepository getRefundRepository() {
        return refundRepository;
    }

    public void setRefundRepository(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }
}
