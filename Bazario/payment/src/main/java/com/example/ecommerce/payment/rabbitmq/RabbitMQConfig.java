package com.example.ecommerce.payment.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQConfig {

    public static final String PAYMENT_QUEUE = "payment_queue";
    public static final String EXCHANGE = "shared_exchange";
    public static final String PAYMENT_ROUTING_KEY = "payment_routing_key";

    @Bean
    public Queue queue() {
        return new Queue(PAYMENT_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(PAYMENT_ROUTING_KEY);
    }
}