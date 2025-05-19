package com.example.ecommerce.payment.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQConfig {

    public static final String PAYMENT_QUEUE = "payment_queue";
    public static final String REFUND_QUEUE = "refund_queue";
    public static final String EXCHANGE = "shared_exchange";
    public static final String PAYMENT_ROUTING_KEY = "payment_routing_key";
    public static final String REFUND_ROUTING_KEY = "refund_routing_key";

    // Define the payment queue
    @Bean(name = "paymentQueue")
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE);
    }

    // Define the refund queue
    @Bean(name = "refundQueue")
    public Queue refundQueue() {
        return new Queue(REFUND_QUEUE);
    }

    // Define the topic exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    // Binding between payment queue and exchange
    @Bean
    public Binding paymentBinding(@Qualifier("paymentQueue") Queue paymentQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(paymentQueue)
                .to(exchange)
                .with(PAYMENT_ROUTING_KEY);
    }

    // Binding between refund queue and exchange
    @Bean
    public Binding refundBinding(@Qualifier("refundQueue") Queue refundQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(refundQueue)
                .to(exchange)
                .with(REFUND_ROUTING_KEY);
    }
}