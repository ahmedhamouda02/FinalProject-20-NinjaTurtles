package com.example.ecommerce.productcart.Models;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "cart")
public class Cart {
    @Id
    private String id;

    private String userId;
    private String productId;
    private int quantity;

    public Cart() {
    }

    public Cart(String id, String userId, String productId, int quantity) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    }