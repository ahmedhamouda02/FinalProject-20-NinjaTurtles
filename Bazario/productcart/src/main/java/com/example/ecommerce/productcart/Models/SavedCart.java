package com.example.ecommerce.productcart.Models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "saved_cart")
public class SavedCart {
    @Id
    private String id;

    private Long userId;
    private List<Product> products;

    public SavedCart() {
    }
    public SavedCart(Long userId, List<Product> products) {
        this.userId = userId;
        this.products = products;
    }
    public SavedCart(String id, Long userId, List<Product> products) {
        this.id = id;
        this.userId = userId;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
