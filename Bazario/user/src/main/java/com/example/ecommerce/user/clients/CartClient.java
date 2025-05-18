package com.example.ecommerce.user.clients;

import com.example.ecommerce.user.DTO.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// @FeignClient(name = "productcart-service", url = "http://localhost:8080") // Replace with actual service URL or Eureka
@FeignClient(name = "productcart-service", url = "http://host.docker.internal:8082")

public interface CartClient {

  // Cart Endpoints
  @GetMapping("/carts/{userId}")
  List<Product> getCart(@PathVariable("userId") Long userId);

  @PostMapping("/carts/add")
  void addToCart(@RequestParam("userId") Long userId,
      @RequestParam("productId") String productId);

  @DeleteMapping("/carts/remove")
  void removeProduct(@RequestParam("userId") Long userId,
      @RequestParam("productId") String productId);

  @DeleteMapping("/carts/clear/{userId}")
  void clearCart(@PathVariable("userId") Long userId);

  // Saved Items
  @GetMapping("/carts/{userId}/saved")
  List<Product> getSavedItems(@PathVariable("userId") Long userId);

  @PostMapping("/carts/save-for-later/{userId}")
  String saveCartForLater(@PathVariable("userId") Long userId);

  @PostMapping("/carts/move-to-cart")
  void moveToCart(@RequestParam("userId") Long userId,
      @RequestParam("productId") String productId);

  @DeleteMapping("/carts/saved/remove")
  void removeSavedItem(@RequestParam("userId") Long userId,
      @RequestParam("productId") String productId);

  @DeleteMapping("/carts/saved/clear/{userId}")
  void clearSavedItems(@PathVariable("userId") Long userId);

  // Checkout
  @PostMapping("/carts/{userId}/checkout")
  Map<String, Object> checkout(@PathVariable("userId") Long userId,
      @RequestBody Map<String, Object> checkoutDetails);

  @PostMapping("/carts/checkout/{userId}")
  Map<String, Object> checkoutCart(@PathVariable("userId") Long userId);
}
