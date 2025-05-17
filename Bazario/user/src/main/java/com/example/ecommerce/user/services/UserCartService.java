package com.example.ecommerce.user.services;

import com.example.ecommerce.user.DTO.Product;
import com.example.ecommerce.user.clients.CartClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class UserCartService {

    @Autowired
    private CartClient cartClient;

    public List<Product> viewCart(Long userId) {
        return cartClient.getCart(userId);
    }

    public void addToCart(Long userId, String productId) {
        cartClient.addToCart(userId, productId);
    }

    public void clearUserCart(Long userId) {
        cartClient.clearCart(userId);
    }

    public List<Product> viewSavedItems(Long userId) {
        return cartClient.getSavedItems(userId);
    }

    public void moveItemToCart(Long userId, String productId) {
        cartClient.moveToCart(userId, productId);
    }

    public Map<String, Object> checkout(Long userId) {
        return cartClient.checkoutCart(userId);
    }
}
