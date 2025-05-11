package com.example.ecommerce.productcart.Models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class ElectronicsProduct extends Product {
    private String brand;
    private Integer warrantyMonths;

    public ElectronicsProduct() {
    }

    public ElectronicsProduct(String name, String category, Double price, String brand, Integer warrantyMonths) {
        super(name, category, price);
        this.brand = brand;
        this.warrantyMonths = warrantyMonths;
    }

    public ElectronicsProduct(String id, String name, String category, Double price, String brand, Integer warrantyMonths) {
        super(id, name, category, price);
        this.brand = brand;
        this.warrantyMonths = warrantyMonths;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(Integer warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }
}
