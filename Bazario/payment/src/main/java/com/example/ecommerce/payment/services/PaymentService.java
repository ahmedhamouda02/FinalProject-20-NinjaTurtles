package com.example.ecommerce.payment.services;

import com.example.ecommerce.payment.command.DiscountPaymentCommand;
import com.example.ecommerce.payment.command.NormalPaymentCommand;
import com.example.ecommerce.payment.models.Payment;
import com.example.ecommerce.payment.repositories.PaymentRepository;
import com.example.ecommerce.payment.command.PaymentCommand;
import com.example.ecommerce.payment.strategy.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public String processPayment(Payment payment, PaymentStrategy paymentStrategy) {
        paymentStrategy.pay();
        PaymentCommand paymentCommand;
        if (payment.getDiscountAmount() > 0) {
            paymentCommand = new DiscountPaymentCommand(payment, paymentRepository);
        } else {
            paymentCommand = new NormalPaymentCommand(payment, paymentRepository);
        }
        paymentCommand.execute();

        return "Payment processed successfully!";
    }

    public PaymentRepository getPaymentRepository() {
        return paymentRepository;
    }

    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
