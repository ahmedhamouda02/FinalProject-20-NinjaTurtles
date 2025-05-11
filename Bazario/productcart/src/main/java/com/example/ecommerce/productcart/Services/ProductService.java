package com.example.ecommerce.productcart.Services;
// import models
import com.example.ecommerce.productcart.Factory.ProductFactory;
import com.example.ecommerce.productcart.Models.*;
import com.example.ecommerce.productcart.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductFactory productFactory;

    public Product createProduct(Map<String, Object> attributes) {
        Product product = productFactory.createProduct(attributes);
        return productRepository.save(product);
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(String id, Map<String, Object> updates) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Common fields
        if (updates.containsKey("name")) existingProduct.setName((String) updates.get("name"));
        if (updates.containsKey("price")) existingProduct.setPrice(((Number) updates.get("price")).doubleValue());

        // Subclass-specific fields
        if (existingProduct instanceof ClothingProduct) {
            ClothingProduct clothing = (ClothingProduct) existingProduct;
            if (updates.containsKey("size")) clothing.setSize((String) updates.get("size"));
            if (updates.containsKey("color")) clothing.setColor((String) updates.get("color"));
            if (updates.containsKey("material")) clothing.setMaterial((String) updates.get("material"));
        } else if (existingProduct instanceof ElectronicsProduct) {
            ElectronicsProduct electronics = (ElectronicsProduct) existingProduct;
            if (updates.containsKey("brand")) electronics.setBrand((String) updates.get("brand"));
            if (updates.containsKey("warranty")) electronics.setWarrantyMonths((int) updates.get("warranty"));
        } else if (existingProduct instanceof FurnitureProduct) {
            FurnitureProduct furniture = (FurnitureProduct) existingProduct;
            if (updates.containsKey("material")) furniture.setMaterial((String) updates.get("material"));
            if (updates.containsKey("dimensions")) furniture.setDimensions((String) updates.get("dimensions"));
        }

        return productRepository.save(existingProduct);
    }
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }



}
