package com.example.ecommerce.productcart.Strategy;

import com.example.ecommerce.productcart.Models.Product;

import java.util.List;

public interface FilterStrategy {
    List<Product> filter(List<Product> products);
}
