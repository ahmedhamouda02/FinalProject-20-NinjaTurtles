package com.example.ecommerce.order.DTO;

import java.util.List;

import java.io.Serializable;
import java.util.List;

public class PaymentsDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Double amount;
    private String discountCode;
    private String paymentMethod;
    private Long userId;
    private List<ItemDTO> items;

    // Constructors
    public PaymentsDTO() {}

    public PaymentsDTO(Double amount, String discountCode, String paymentMethod, Long userId, List<ItemDTO> items) {
        this.amount = amount;
        this.discountCode = discountCode;
        this.paymentMethod = paymentMethod;
        this.userId = userId;
        this.items = items;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}
