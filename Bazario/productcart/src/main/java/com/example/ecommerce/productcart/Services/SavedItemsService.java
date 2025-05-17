package com.example.ecommerce.productcart.Services;

import com.example.ecommerce.productcart.Models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavedItemsService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CartService cartService;

    private String getSavedItemsKey(Long userId) {
        return "saved:" + userId;
    }

    public List<Product> getSavedItems(Long userId) {
        String key = getSavedItemsKey(userId);
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        List<Object> raw = listOps.range(key, 0, -1);
        return raw.stream()
                .map(obj -> (Product) obj)
                .collect(Collectors.toList());
    }

    public void saveForLater(Long userId, String productId) {
        // Get the product from the cart
        List<Product> cart = cartService.getCart(userId);

        // Find the product in the cart
        Product productToSave = cart.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (productToSave != null) {
            // Add to saved items
            redisTemplate.opsForList().rightPush(getSavedItemsKey(userId), productToSave);

            // Remove from cart
            cartService.removeProduct(userId, productId);
        }
    }

    public void moveToCart(Long userId, String productId) {
        // Get saved items
        List<Product> savedItems = getSavedItems(userId);

        // Find the product in saved items
        Product productToMove = savedItems.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (productToMove != null) {
            // Add to cart
            cartService.addToCart(userId, productToMove);

            // Remove from saved items
            removeSavedItem(userId, productId);
        }
    }

    public void removeSavedItem(Long userId, String productId) {
        List<Product> savedItems = getSavedItems(userId);
        savedItems.removeIf(p -> p.getId().equals(productId));

        // Clear and rebuild saved items list
        redisTemplate.delete(getSavedItemsKey(userId));
        savedItems.forEach(p -> redisTemplate.opsForList().rightPush(getSavedItemsKey(userId), p));
    }

    public void clearSavedItems(Long userId) {
        redisTemplate.delete(getSavedItemsKey(userId));
    }
}