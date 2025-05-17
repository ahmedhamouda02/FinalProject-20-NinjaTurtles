package com.example.ecommerce.productcart.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String id;
    private String userId;
    private List<Product> products;
    private Double totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private String shippingAddress;
    private String paymentMethod;
}