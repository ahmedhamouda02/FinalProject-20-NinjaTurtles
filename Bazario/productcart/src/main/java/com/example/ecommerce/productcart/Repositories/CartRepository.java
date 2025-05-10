package com.example.ecommerce.productcart.Repositories;

import com.example.ecommerce.productcart.Models.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartRepository extends MongoRepository<Cart, String> {
    List<Cart> findByUserId(String userId);
    Cart findByProductIdAndUserId(String productId, String userId);
    void deleteByProductIdAndUserId(String userId, String productId);
    void deleteByUserId(String userId);
}
