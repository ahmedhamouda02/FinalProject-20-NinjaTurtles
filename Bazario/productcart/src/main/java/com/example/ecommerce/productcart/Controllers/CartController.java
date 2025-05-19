package com.example.ecommerce.productcart.Controllers;

import com.example.ecommerce.productcart.Models.Product;
import com.example.ecommerce.productcart.Services.CartService;
import com.example.ecommerce.productcart.Services.CheckoutService;
import com.example.ecommerce.productcart.Services.SavedItemsService;
import com.example.ecommerce.productcart.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carts")
public class CartController {
  @Autowired
  private CartService cartService;

  @Autowired
  private SavedItemsService savedItemsService;

  @Autowired
  private CheckoutService checkoutService;

  @Autowired
  private ProductRepository productRepository;

  @GetMapping
  public ResponseEntity<List<Product>> getCart(
      @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
      @RequestParam(value = "userId", required = false) Long pathUserId) {
    Long userId = resolveUserId(headerUserId, pathUserId);
    return ResponseEntity.ok(cartService.getCart(userId));
  }

  @PostMapping("/add")
  public ResponseEntity<?> addToCart(
      @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
      @RequestParam(value = "userId", required = false) Long paramUserId,
      @RequestParam String productId) {
    Long userId = resolveUserId(headerUserId, paramUserId);

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Product not found"));

    cartService.addToCart(userId, product);
    return ResponseEntity.ok("Product added to cart");
  }

  @DeleteMapping("/remove")
  public ResponseEntity<?> removeProduct(@RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
      @RequestParam(value = "userId", required = false) Long paramUserId, @RequestParam String productId) {
    Long userId = resolveUserId(headerUserId, paramUserId);
    cartService.removeProduct(userId, productId);
    return ResponseEntity.ok("Product removed from cart");
  }

  @DeleteMapping("/clear")
  public ResponseEntity<?> clearCart(@RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
      @RequestParam(value = "userId", required = false) Long pathUserId) {
    Long userId = resolveUserId(headerUserId, pathUserId);
    cartService.clearCart(userId);
    return ResponseEntity.ok("Cart cleared");
  }

  // New endpoints for saved items
  @GetMapping("/saved")
  public ResponseEntity<List<Product>> getSavedItems(
      @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
      @RequestParam(value = "userId", required = false) Long pathUserId) {
    Long userId = resolveUserId(headerUserId, pathUserId);
    return ResponseEntity.ok(savedItemsService.getSavedItems(userId));
  }

  @PostMapping("/save-for-later")
  public ResponseEntity<String> saveCartForLater(
      @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
      @RequestParam(value = "userId", required = false) Long pathUserId) {
    Long userId = resolveUserId(headerUserId, pathUserId);
    String message = cartService.saveCartForLater(userId);
    return ResponseEntity.ok(message);
  }

  @PostMapping("/move-to-cart")
  public ResponseEntity<?> moveToCart(@RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
      @RequestParam(value = "userId", required = false) Long pathUserId, @RequestParam String productId) {
    Long userId = resolveUserId(headerUserId, pathUserId);
    savedItemsService.moveToCart(userId, productId);
    return ResponseEntity.ok("Product moved to cart");
  }

  @DeleteMapping("/saved/remove")
  public ResponseEntity<?> removeSavedItem(@RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
      @RequestParam(value = "userId", required = false) Long pathUserId, @RequestParam String productId) {
    Long userId = resolveUserId(headerUserId, pathUserId);
    savedItemsService.removeSavedItem(userId, productId);
    return ResponseEntity.ok("Saved item removed");
  }

  @DeleteMapping("/saved/clear")
  public ResponseEntity<?> clearSavedItems(@RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
      @RequestParam(value = "userId", required = false) Long pathUserId) {
    Long userId = resolveUserId(headerUserId, pathUserId);
    savedItemsService.clearSavedItems(userId);
    return ResponseEntity.ok("Saved items cleared");
  }

  // // Checkout endpoint
  // @PostMapping("/{userId}/checkout")
  // public ResponseEntity<?> checkout(@PathVariable Long userId, @RequestBody
  // Map<String, Object> checkoutDetails) {
  // try {
  // Map<String, Object> result = checkoutService.checkout(userId,
  // checkoutDetails);
  // return ResponseEntity.ok(result);
  // } catch (IllegalStateException e) {
  // return ResponseEntity.badRequest().body(e.getMessage());
  // } catch (Exception e) {
  // return ResponseEntity.internalServerError().body("Checkout failed: " +
  // e.getMessage());
  // }
  // }

  @PostMapping("/checkout/{userId}")
  public ResponseEntity<?> checkoutCart(@PathVariable Long userId) {
    try {
      double total = cartService.checkoutCart(userId);
      return ResponseEntity.ok(Map.of(
          "userId", userId,
          "totalPrice", total));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * Process checkout with payment options
   */
  @PostMapping("/checkout")
  public ResponseEntity<?> checkout(
      @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
      @RequestParam(value = "userId", required = false) Long pathUserId,

      @RequestParam(defaultValue = "NO_DISCOUNT") String discountCode,
      @RequestParam(defaultValue = "CREDIT_CARD") String paymentMethod) {
    try {
      Long userId = resolveUserId(headerUserId, pathUserId);
      Map<String, Object> result = cartService.checkout(userId, discountCode, paymentMethod);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  private Long resolveUserId(Long headerId, Long paramId) {
    if (headerId != null) {
      return headerId;
    }
    if (paramId != null) {
      return paramId;
    }
    throw new IllegalArgumentException("Missing userId (either in X-User-Id header or request parameter)");
  }

}
