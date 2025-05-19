package com.example.ecommerce.payment.services;

import com.example.ecommerce.payment.DTO.RefundDTO;
import com.example.ecommerce.payment.models.Payment;
import com.example.ecommerce.payment.models.Refund;
import com.example.ecommerce.payment.rabbitmq.RabbitMQConfig;
import com.example.ecommerce.payment.repositories.RefundRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public Refund makeRefund(RefundDTO refundDTO) {
        Double amount= refundDTO.getAmount();
        Long userId = refundDTO.getUserId();
        Refund refund = new Refund();
        refund.setPoints(calculatePoints(amount));
        refund.setUserId(userId);
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
