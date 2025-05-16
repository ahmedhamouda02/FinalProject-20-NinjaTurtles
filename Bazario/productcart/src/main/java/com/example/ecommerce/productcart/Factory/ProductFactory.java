package com.example.ecommerce.productcart.Factory;

import com.example.ecommerce.productcart.Models.*;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class ProductFactory {

    public Product createProduct(Map<String, Object> attributes) {
        String category = ((String) attributes.get("category")).toLowerCase();

        switch (category) {
            case "clothing":
                return new ClothingProduct(
                        (String) attributes.get("name"),
                        category,
                        ((Number) attributes.get("price")).doubleValue(),
                        (String) attributes.get("size"),
                        (String) attributes.get("color"),
                        (String) attributes.get("material")
                );
            case "electronics":
                return new ElectronicsProduct(
                        (String) attributes.get("name"),
                        category,
                        ((Number) attributes.get("price")).doubleValue(),
                        (String) attributes.get("brand"),
                        ((Number) attributes.get("warranty")).intValue()
                );
            case "furniture":
                return new FurnitureProduct(
                        (String) attributes.get("name"),
                        category,
                        ((Number) attributes.get("price")).doubleValue(),
                        (String) attributes.get("material"),
                        (String) attributes.get("dimensions")
                );
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }
    }
}
