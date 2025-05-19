package com.example.ecommerce.payment.services;

import com.example.ecommerce.payment.models.Payment;
import com.example.ecommerce.payment.models.Refund;
import com.example.ecommerce.payment.repositories.RefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return (int) (amount / 10);
    }

    // Fetch all refunds
    public List<Refund> getAllRefunds() {
        return refundRepository.findAll();
    }
    // Get total points for a user by userId
    public Integer getTotalPointsByUserId(Long userId) {
        return refundRepository.sumPointsByUserId(userId);
    }

    public RefundRepository getRefundRepository() {
        return refundRepository;
    }

    public void setRefundRepository(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }}
