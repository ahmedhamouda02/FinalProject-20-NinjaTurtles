package com.example.ecommerce.productcart.Services;

import com.example.ecommerce.productcart.Models.Product;
import com.example.ecommerce.productcart.Models.SavedCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    private String getCartKey(Long userId) {
        return "cart:" + userId;
    }

    public List<Product> getCart(Long userId) {
        String key = getCartKey(userId);
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        List<Object> raw = listOps.range(key, 0, -1);
        return raw.stream()
                .map(obj -> (Product) obj)
                .collect(Collectors.toList());
    }

    public void addToCart(Long userId, Product product) {
        redisTemplate.opsForList().rightPush(getCartKey(userId), product);
    }

    public void removeProduct(Long userId, String productId) {
        List<Product> cart = getCart(userId);
        cart.removeIf(p -> p.getId().equals(productId));
        redisTemplate.delete(getCartKey(userId));
        cart.forEach(p -> addToCart(userId, p));
    }

    public void clearCart(Long userId) {
        redisTemplate.delete(getCartKey(userId));
    }

    public String saveCartForLater(Long userId) {
        String key = getCartKey(userId);
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        List<Object> rawItems = listOps.range(key, 0, -1);

        if (rawItems == null || rawItems.isEmpty()) {
            return "No items in cart to save.";
        }

        List<Product> cartItems = rawItems.stream()
                .map(obj -> (Product) obj)
                .collect(Collectors.toList());

        // Save to MongoDB
        SavedCart savedCart = new SavedCart();
        savedCart.setUserId(userId);
        savedCart.setProducts(cartItems);

        mongoTemplate.save(savedCart);

        // Clear the cart from Redis
        redisTemplate.delete(key);

        return "Cart saved for later successfully.";
    }

    public double checkoutCart(Long userId) {
        List<Product> cartItems = getCart(userId);

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty for user: " + userId);
        }

        return cartItems.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

}
