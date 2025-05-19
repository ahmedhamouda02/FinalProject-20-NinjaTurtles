package com.example.ecommerce.user.controllers;

import com.example.ecommerce.user.DTO.Product;
import com.example.ecommerce.user.models.LoginRequest;
import com.example.ecommerce.user.models.User;
import com.example.ecommerce.user.services.UserCartService;
import com.example.ecommerce.user.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserCartService userCartService;

  // Register a new user
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody User user) {
    try {
      User registeredUser = userService.register(user);
      return ResponseEntity.ok(registeredUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }

  // Login user (cache on success)
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
      String token = userService.login(request.getEmail(), request.getPassword());
      return ResponseEntity.ok(Map.of("token", token));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
    }
  }

  // Logout user (evicts from cache)
  @PostMapping("/logout")
  public ResponseEntity<?> logout(
      @RequestHeader("Authorization") String authHeader) {
    // authHeader == "Bearer eyJ..."
    String token = authHeader.substring(7);
    boolean ok = userService.logout(token);
    return ok
        ? ResponseEntity.ok("Logged out successfully")
        : ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Invalid or already logged-out session");
  }

  // Check if user is logged in
  @GetMapping("/status")
  public ResponseEntity<String> isLoggedIn(HttpServletRequest request) {
    String userIdHeader = request.getHeader("X-User-Id");
    if (userIdHeader == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing user ID");
    }
    Long id = Long.parseLong(userIdHeader);
    System.out.println("Checking login status for user ID: " + id);
    boolean isLoggedIn = userService.isUserLoggedIn(id);
    return ResponseEntity.ok(isLoggedIn ? "User is logged in" : "User is not logged in");
  }

  // Get user by ID
  @GetMapping("/{id}")
  public ResponseEntity<?> getUserById(@PathVariable Long id) {
    Optional<User> user = userService.getUserById(id);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  // Get all users
  @GetMapping
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  // Update user
  @PutMapping("/{id}")
  public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
    Optional<User> user = userService.updateUser(id, updatedUser);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  // Delete user
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable Long id) {
    boolean deleted = userService.deleteUser(id);
    return deleted
        ? ResponseEntity.ok("User deleted successfully")
        : ResponseEntity.notFound().build();
  }

  // Reset password
  @PostMapping("/reset-password")
  public ResponseEntity<String> resetPassword(
      @RequestHeader("X-User-Id") Long userId,
      @RequestParam String newPassword) {
    boolean ok = userService.resetPassword(userId, newPassword);
    if (ok) {
      return ResponseEntity.ok("Password reset successful");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("User not found");
    }
  }

  /** View my cart */
  @GetMapping("/viewCart")
  public ResponseEntity<List<Product>> viewCart(
      @RequestHeader("X-User-Id") Long userId) {
    return ResponseEntity.ok(userCartService.viewCart(userId));
  }

  /** Add a product to my cart */
  @PostMapping("/add")
  public ResponseEntity<String> addToCart(
      @RequestHeader("X-User-Id") Long userId,
      @RequestParam String productId) {
    userCartService.addToCart(userId, productId);
    return ResponseEntity.ok("Product added to cart");
  }

  /** Clear my cart */
  @DeleteMapping("/clear")
  public ResponseEntity<String> clearCart(
      @RequestHeader("X-User-Id") Long userId) {
    userCartService.clearUserCart(userId);
    return ResponseEntity.ok("Cart cleared");
  }

  /** View my saved items */
  @GetMapping("/saved")
  public ResponseEntity<List<Product>> viewSavedItems(
      @RequestHeader("X-User-Id") Long userId) {
    return ResponseEntity.ok(userCartService.viewSavedItems(userId));
  }

  /** Move one of my saved items into the cart */
  @PostMapping("/move-to-cart")
  public ResponseEntity<String> moveToCart(
      @RequestHeader("X-User-Id") Long userId,
      @RequestParam String productId) {
    userCartService.moveItemToCart(userId, productId);
    return ResponseEntity.ok("Product moved to cart");
  }

  /** Checkout my cart */
  @PostMapping("/checkout")
  public ResponseEntity<Map<String, Object>> checkout(
      @RequestHeader("X-User-Id") Long userId) {
    return ResponseEntity.ok(userCartService.checkout(userId));
  }
}
