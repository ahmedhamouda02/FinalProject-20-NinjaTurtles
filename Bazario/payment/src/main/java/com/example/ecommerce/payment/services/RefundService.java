package com.example.ecommerce.payment.services;

import com.example.ecommerce.payment.DTO.RefundDTO;
import com.example.ecommerce.payment.models.Payment;
import com.example.ecommerce.payment.models.Refund;
import com.example.ecommerce.payment.rabbitmq.RabbitMQConfig;
import com.example.ecommerce.payment.repositories.RefundRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public void makeRefund(String json) {
        try {
            System.out.println("Received payment message JSON: " + json);

            ObjectMapper mapper = new ObjectMapper();
            System.out.println("Received JSON: " + json);
            RefundDTO refundDTO = new ObjectMapper().readValue(json, RefundDTO.class);

            Double amount= refundDTO.getAmount();
            Long userId = refundDTO.getUserId();
            Refund refund = new Refund();
            refund.setPoints(calculatePoints(amount));
            refund.setUserId(userId);
            refundRepository.save(refund);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


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
