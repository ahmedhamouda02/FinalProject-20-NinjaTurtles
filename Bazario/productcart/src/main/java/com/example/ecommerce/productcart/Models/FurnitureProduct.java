package com.example.ecommerce.productcart.Models;

import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
public class FurnitureProduct extends Product{
    private String material;
    private String dimensions;


    public FurnitureProduct(String id, String name, String category, Double price, String material, String dimensions) {
        super(id, name, category, price);
        this.material = material;
        this.dimensions = dimensions;
    }

    public FurnitureProduct(String name, String category, Double price, String material, String dimensions) {
        super(name, category, price);
        this.material = material;
        this.dimensions = dimensions;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

}
