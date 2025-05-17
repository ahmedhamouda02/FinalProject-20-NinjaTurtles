package com.example.ecommerce.user.controllers;

import com.example.ecommerce.user.DTO.Product;
import com.example.ecommerce.user.services.UserCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user-cart") // Base path for cart-related operations in user-service
public class UserCartController {

  @Autowired
  private UserCartService userCartService;

  @GetMapping("/{userId}")
  public ResponseEntity<List<Product>> viewCart(@PathVariable Long userId) {
    return ResponseEntity.ok(userCartService.viewCart(userId));
  }

  @PostMapping("/add")
  public ResponseEntity<String> addToCart(@RequestParam Long userId, @RequestParam String productId) {
    userCartService.addToCart(userId, productId);
    return ResponseEntity.ok("Product added to cart");
  }

  @DeleteMapping("/clear/{userId}")
  public ResponseEntity<String> clearCart(@PathVariable Long userId) {
    userCartService.clearUserCart(userId);
    return ResponseEntity.ok("Cart cleared");
  }

  @GetMapping("/{userId}/saved")
  public ResponseEntity<List<Product>> viewSavedItems(@PathVariable Long userId) {
    return ResponseEntity.ok(userCartService.viewSavedItems(userId));
  }

  @PostMapping("/move-to-cart")
  public ResponseEntity<String> moveToCart(@RequestParam Long userId, @RequestParam String productId) {
    userCartService.moveItemToCart(userId, productId);
    return ResponseEntity.ok("Product moved to cart");
  }

  @PostMapping("/checkout/{userId}")
  public ResponseEntity<Map<String, Object>> checkout(@PathVariable Long userId) {
    return ResponseEntity.ok(userCartService.checkout(userId));
  }
}
