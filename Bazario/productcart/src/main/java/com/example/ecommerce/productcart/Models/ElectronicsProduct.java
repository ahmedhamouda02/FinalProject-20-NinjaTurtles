package com.example.ecommerce.productcart.Models;

import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
public class ElectronicsProduct extends Product {
    private String brand;
    private Integer warrantyMonths;



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
