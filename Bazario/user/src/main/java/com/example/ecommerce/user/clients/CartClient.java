package com.example.ecommerce.user.clients;

import com.example.ecommerce.user.DTO.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "productcart-service", url = "http://host.docker.internal:8082")
public interface CartClient {

  // Cart Endpoints
  @GetMapping("/carts")
  List<Product> getCart(
          @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
          @RequestParam(value = "userId", required = false) Long paramUserId);

  @PostMapping("/carts/add")
  void addToCart(
          @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
          @RequestParam(value = "userId", required = false) Long paramUserId,
          @RequestParam("productId") String productId);

  @DeleteMapping("/carts/remove")
  void removeProduct(
          @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
          @RequestParam(value = "userId", required = false) Long paramUserId,
          @RequestParam("productId") String productId);

  @DeleteMapping("/carts/clear")
  void clearCart(
          @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
          @RequestParam(value = "userId", required = false) Long paramUserId);

  // Saved Items
  @GetMapping("/carts/saved")
  List<Product> getSavedItems(
          @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
          @RequestParam(value = "userId", required = false) Long paramUserId);

  @PostMapping("/carts/save-for-later")
  String saveCartForLater(
          @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
          @RequestParam(value = "userId", required = false) Long paramUserId);

  @PostMapping("/carts/move-to-cart")
  void moveToCart(
          @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
          @RequestParam(value = "userId", required = false) Long paramUserId,
          @RequestParam("productId") String productId);

  @DeleteMapping("/carts/saved/remove")
  void removeSavedItem(
          @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
          @RequestParam(value = "userId", required = false) Long paramUserId,
          @RequestParam("productId") String productId);

  @DeleteMapping("/carts/saved/clear")
  void clearSavedItems(
          @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
          @RequestParam(value = "userId", required = false) Long paramUserId);

  // Checkout
  @PostMapping("/carts/checkout")
  Map<String, Object> checkout(
          @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
          @RequestParam(value = "userId", required = false) Long paramUserId,
          @RequestParam(defaultValue = "NO_DISCOUNT") String discountCode,
          @RequestParam(defaultValue = "CREDIT_CARD") String paymentMethod);

  @PostMapping("/carts/checkout/{userId}")
  Map<String, Object> checkoutCart(@PathVariable("userId") Long userId);
}
