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
    return cartClient.getCart(userId, null);
  }

  public void addToCart(Long userId, String productId) {
    cartClient.addToCart(userId, null, productId);
  }

  public void clearUserCart(Long userId) {
    cartClient.clearCart(userId, null);
  }

  public List<Product> viewSavedItems(Long userId) {
    return cartClient.getSavedItems(userId, null);
  }

  public void moveItemToCart(Long userId, String productId) {
    cartClient.moveToCart(userId, null, productId);
  }

  public Map<String, Object> checkout(Long userId) {
    try {
      return cartClient.checkout(userId, null, "NO_DISCOUNT", "CREDIT_CARD");
    } catch (Exception e) {
      throw new IllegalArgumentException("Checkout failed: Cart is empty or invalid");
    }
  }
}
