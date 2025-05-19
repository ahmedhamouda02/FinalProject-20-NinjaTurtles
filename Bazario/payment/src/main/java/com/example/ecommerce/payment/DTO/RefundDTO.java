package com.example.ecommerce.payment.DTO;

public class RefundDTO {
    private Long userId;
    private Double amount;

    // Constructors
    public RefundDTO() {}

    public RefundDTO(Long userId, Double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
