package com.example.ecommerce.payment.strategy;

public class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay() {
        System.out.println("Paying with Credit Card");
    }
}
