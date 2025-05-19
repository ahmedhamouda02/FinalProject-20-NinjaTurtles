package com.example.ecommerce.payment.rabbitmq;

import com.example.ecommerce.payment.DTO.PaymentsDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendToOrder(PaymentsDTO PaymentsDTO) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.PAYMENT_ROUTING_KEY,
                PaymentsDTO
        );
        System.out.println(PaymentsDTO);
    }
}