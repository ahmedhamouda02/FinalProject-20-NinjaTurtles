package com.example.Paymentgp.service;

import com.example.Paymentgp.model.Payment;
import com.example.Paymentgp.repository.PaymentRepository;
import com.example.Paymentgp.command.NormalPaymentCommand;
import com.example.Paymentgp.command.DiscountPaymentCommand;
import com.example.Paymentgp.command.PaymentCommand;
import com.example.Paymentgp.strategy.PaymentStrategy;
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
