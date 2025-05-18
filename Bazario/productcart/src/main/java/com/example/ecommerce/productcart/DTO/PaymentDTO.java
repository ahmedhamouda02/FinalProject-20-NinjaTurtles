package com.example.ecommerce.productcart.DTO;

public class PaymentDTO {
    private Double amount;
    private String discountCode;
    private String paymentMethod;

    // Constructors
    public PaymentDTO() {}

    public PaymentDTO(Double amount, String discountCode, String paymentMethod) {
        this.amount = amount;
        this.discountCode = discountCode;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}