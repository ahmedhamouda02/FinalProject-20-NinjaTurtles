package com.example.ecommerce.payment.command;

import com.example.ecommerce.payment.models.Payment;
import com.example.ecommerce.payment.repositories.PaymentRepository;

public class DiscountPaymentCommand implements PaymentCommand {
    private final Payment payment;
    private final PaymentRepository paymentRepository;

    public DiscountPaymentCommand(Payment payment, PaymentRepository paymentRepository) {
        this.payment = payment;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void execute() {
        double discount = payment.getDiscountAmount();
        double original = payment.getAmount();
        double discounted = original * (1 - discount);
        payment.setAmount(discounted);
        paymentRepository.save(payment);
    }
}
