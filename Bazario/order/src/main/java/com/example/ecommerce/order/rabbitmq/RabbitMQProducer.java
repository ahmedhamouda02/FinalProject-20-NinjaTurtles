package com.example.ecommerce.order.rabbitmq;

import com.example.ecommerce.order.DTO.RefundDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendRefund(RefundDTO refundDTO) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.REFUND_ROUTING_KEY,
                refundDTO
        );
        System.out.println(refundDTO);
    }
}