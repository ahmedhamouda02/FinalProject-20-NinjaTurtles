package com.example.ecommerce.payment.services;

import com.example.ecommerce.payment.DTO.PaymentsDTO;
import com.example.ecommerce.payment.command.DiscountPaymentCommand;
import com.example.ecommerce.payment.command.NormalPaymentCommand;
import com.example.ecommerce.payment.models.Payment;
import com.example.ecommerce.payment.repositories.PaymentRepository;
import com.example.ecommerce.payment.command.PaymentCommand;
import com.example.ecommerce.payment.strategy.CreditCardPayment;
import com.example.ecommerce.payment.strategy.PayPalPayment;
import com.example.ecommerce.payment.strategy.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ecommerce.payment.rabbitmq.RabbitMQProducer;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private RabbitMQProducer rabbitMQProducer;
    @Autowired
    public PaymentService(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }
    public String processPayment(PaymentsDTO paymentDTO) {
         Double amount = paymentDTO.getAmount();
         String discountCode = paymentDTO.getDiscountCode();
         String paymentMethod = paymentDTO.getPaymentMethod();
        Payment payment =  new Payment(amount,discountCode,paymentMethod);
        paymentRepository.save(payment);
        PaymentStrategy paymentStrategy = determinePaymentMethod(payment);
        paymentStrategy.pay();
        PaymentCommand paymentCommand;
        if (payment.getDiscountAmount() > 0) {
            paymentCommand = new DiscountPaymentCommand(payment, paymentRepository);
        } else {
            paymentCommand = new NormalPaymentCommand(payment, paymentRepository);
        }
        paymentCommand.execute();
        rabbitMQProducer.sendToOrder(paymentDTO);
        return "Payment processed successfully!";
    }

    private PaymentStrategy determinePaymentMethod(Payment payment) {
        if ("CREDIT_CARD".equalsIgnoreCase(payment.getPaymentMethod())) {
            return new CreditCardPayment();
        } else if ("PAYPAL".equalsIgnoreCase(payment.getPaymentMethod())) {
            return new PayPalPayment();
        } else {
            throw new IllegalArgumentException("Invalid payment method");
        }
    }

    public PaymentRepository getPaymentRepository() {
        return paymentRepository;
    }

    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
