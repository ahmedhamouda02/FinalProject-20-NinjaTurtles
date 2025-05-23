// Product Table
package com.example.ecommerce.productcart.Models;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "products")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // This will be stored in Redis JSON
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClothingProduct.class, name = "clothing"),
        @JsonSubTypes.Type(value = ElectronicsProduct.class, name = "electronics"),
        @JsonSubTypes.Type(value = FurnitureProduct.class, name = "furniture")
})
public abstract class Product {
    @Id
    private String id;

    private String name;
    private String category;
    private Double price;

    // 3 constructors
    public Product() {
    }
    public Product(String id, String name, String category, Double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }
    public Product(String name, String category, Double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // Getters and Setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}