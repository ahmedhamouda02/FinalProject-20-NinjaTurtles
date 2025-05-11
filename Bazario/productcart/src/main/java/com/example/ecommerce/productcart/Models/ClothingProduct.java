package com.example.ecommerce.productcart.Models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class ClothingProduct  extends Product{
    private String size;
    private String color;
    private String material;

    public ClothingProduct() {
    }

    public ClothingProduct(String id, String name, String category, Double price, String size, String color, String material) {
        super(id, name, category, price);
        this.size = size;
        this.color = color;
        this.material = material;
    }

    public ClothingProduct(String name, String category, Double price, String size, String color, String material) {
        super(name, category, price);
        this.size = size;
        this.color = color;
        this.material = material;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

}
