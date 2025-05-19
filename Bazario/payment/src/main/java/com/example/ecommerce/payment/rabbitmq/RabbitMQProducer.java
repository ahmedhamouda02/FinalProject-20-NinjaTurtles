package com.example.ecommerce.payment.rabbitmq;

import com.example.ecommerce.payment.DTO.PaymentsDTO;
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

    public void sendToOrder(PaymentsDTO paymentDto) {
        try {
            String json = objectMapper.writeValueAsString(paymentDto);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.PAYMENT_ROUTING_KEY,
                    json
            );
            System.out.println("Sent JSON: " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}