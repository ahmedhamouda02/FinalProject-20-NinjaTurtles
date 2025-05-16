package com.example.Paymentgp.strategy;

public class PayPalPayment implements PaymentStrategy {
    @Override
    public void pay() {
        System.out.println("Paying with PayPal");
    }
}
