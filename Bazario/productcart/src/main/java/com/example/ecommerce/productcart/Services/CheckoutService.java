package com.example.ecommerce.productcart.Services;

import com.example.ecommerce.productcart.Models.Order;
import com.example.ecommerce.productcart.Models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CheckoutService {

    @Autowired
    private CartService cartService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${order.service.url:http://localhost:8084/orders}")
    private String orderServiceUrl;

    public Map<String, Object> checkout(Long userId, Map<String, Object> checkoutDetails) {
        // 1. Get cart items
        List<Product> cartItems = cartService.getCart(userId);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart");
        }

        // 2. Calculate cart total
        double total = cartItems.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        // 3. Prepare order data
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("userId", userId);
        orderData.put("products", cartItems);
        orderData.put("total", total);
        orderData.put("shippingAddress", checkoutDetails.get("shippingAddress"));
        orderData.put("paymentMethod", checkoutDetails.get("paymentMethod"));
        orderData.put("orderId", UUID.randomUUID().toString());

        // 4. Send to Order service (this would typically use Feign or messaging)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(orderData, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(
                    orderServiceUrl, request, Map.class);

            // 5. Clear cart after successful checkout
            cartService.clearCart(userId);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Checkout failed: " + e.getMessage(), e);
        }
    }
}