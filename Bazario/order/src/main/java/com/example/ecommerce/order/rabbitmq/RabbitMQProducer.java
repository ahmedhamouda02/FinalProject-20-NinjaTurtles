package com.example.ecommerce.order.rabbitmq;

import com.example.ecommerce.order.DTO.RefundDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    public void sendRefund(RefundDTO refundDTO) {
        try {
        String json = objectMapper.writeValueAsString(refundDTO);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.REFUND_ROUTING_KEY,
                json
        );
        System.out.println(json);
    } catch (
    JsonProcessingException e) {
        e.printStackTrace();
    }
    }
}