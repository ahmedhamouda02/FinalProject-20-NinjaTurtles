package com.example.ecommerce.productcart.Models;

import java.io.Serializable;

public class Cart implements Serializable {
    private Long userId;
    private String productId;
    private int quantity;

    public Cart() {
    }

    public Cart(Long userId, String productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
