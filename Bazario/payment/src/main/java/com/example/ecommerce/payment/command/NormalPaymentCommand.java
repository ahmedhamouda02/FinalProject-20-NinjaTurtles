package com.example.ecommerce.payment.command;

import com.example.ecommerce.payment.models.Payment;
import com.example.ecommerce.payment.repositories.PaymentRepository;

public class NormalPaymentCommand implements PaymentCommand {
    private Payment payment;
    private PaymentRepository paymentRepository;

    public NormalPaymentCommand(Payment payment, PaymentRepository paymentRepository) {
        this.payment = payment;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void execute() {
        paymentRepository.save(payment); // Save the normal payment in DB
    }

    // Getters and Setters
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public PaymentRepository getPaymentRepository() {
        return paymentRepository;
    }

    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
