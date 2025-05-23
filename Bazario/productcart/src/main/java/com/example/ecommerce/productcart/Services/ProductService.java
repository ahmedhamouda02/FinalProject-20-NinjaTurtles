package com.example.ecommerce.productcart.Services;
// import models
import com.example.ecommerce.productcart.Factory.ProductFactory;
import com.example.ecommerce.productcart.Models.*;
import com.example.ecommerce.productcart.Repositories.CartRepository;
import com.example.ecommerce.productcart.Repositories.ProductRepository;
import com.example.ecommerce.productcart.Strategy.ClothingFilterStrategy;
import com.example.ecommerce.productcart.Strategy.ElectronicsFilterStrategy;
import com.example.ecommerce.productcart.Strategy.FurnitureFilterStrategy;
import com.example.ecommerce.productcart.Strategy.FilterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductFactory productFactory;

    private final ProductFilterContext productFilterContext;

    // Inject the context through constructor
    @Autowired
    public ProductService(ProductFilterContext productFilterContext) {
        this.productFilterContext = productFilterContext;
    }

    public List<Product> getProductsSortedByPrice(boolean ascending) {
        Sort sort = ascending ? Sort.by("price").ascending() : Sort.by("price").descending();
        return productRepository.findAll(sort);
    }
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public List<Product> getFilteredProducts(String category) {
        List<Product> products = productRepository.findAll();  // Fetch from MongoDB

        try {
            // Construct full class name using reflection
            String className = "com.example.ecommerce.productcart.Strategy." +
                    capitalize(category) + "FilterStrategy";

            // Dynamically load class and create instance
            Class<?> clazz = Class.forName(className);
            FilterStrategy filterStrategy = (FilterStrategy) clazz.getDeclaredConstructor().newInstance();

            // Set and use the strategy
            productFilterContext.setFilterStrategy(filterStrategy);
            return productFilterContext.executeFilter(products);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("No filter strategy found for category: " + category);
        } catch (Exception e) {
            throw new RuntimeException("Failed to apply filter strategy: " + e.getMessage());
        }
    }



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
