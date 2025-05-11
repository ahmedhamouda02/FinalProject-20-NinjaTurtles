package com.example.ecommerce.productcart.Strategy;

import com.example.ecommerce.productcart.Models.Product;
import java.util.List;
import java.util.stream.Collectors;

public class FurnitureFilterStrategy implements FilterStrategy {

    @Override
    public List<Product> filter(List<Product> products) {
        return products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase("Furniture"))
                .collect(Collectors.toList());
    }
}
