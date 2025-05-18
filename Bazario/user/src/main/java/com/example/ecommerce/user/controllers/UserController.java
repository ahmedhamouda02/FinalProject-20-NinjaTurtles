package com.example.ecommerce.user.controllers;

import com.example.ecommerce.user.DTO.Product;
import com.example.ecommerce.user.models.User;
import com.example.ecommerce.user.services.UserCartService;
import com.example.ecommerce.user.services.UserService;
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
  public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
    User user = userService.login(email, password);
    if (user != null) {
      return ResponseEntity.ok("Login successful. User ID: " + user.getId());
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
  }

  // Logout user (evicts from cache)
  @PostMapping("/{id}/logout")
  public ResponseEntity<String> logout(@PathVariable Long id) {
    boolean result = userService.logout(id);
    return result
        ? ResponseEntity.ok("Logout successful")
        : ResponseEntity.badRequest().body("User was not logged in or doesn't exist.");
  }

  // Check if user is logged in (based on Redis cache)
  @GetMapping("/{id}/status")
  public ResponseEntity<String> isLoggedIn(@PathVariable Long id) {
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
  @PostMapping("/{id}/reset-password")
  public ResponseEntity<String> resetPassword(
      @PathVariable Long id,
      @RequestParam String newPassword) {
    boolean ok = userService.resetPassword(id, newPassword);
    if (ok) {
      return ResponseEntity.ok("Password reset successful");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("User not found");
    }
  }

  @GetMapping("/viewCart/{userId}")
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
