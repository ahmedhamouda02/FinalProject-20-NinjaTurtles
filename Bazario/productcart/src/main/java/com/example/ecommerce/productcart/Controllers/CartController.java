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
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private SavedItemsService savedItemsService;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Product>> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam String userId, @RequestParam String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartService.addToCart(userId, product);
        return ResponseEntity.ok("Product added to cart");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeProduct(@RequestParam String userId, @RequestParam String productId) {
        cartService.removeProduct(userId, productId);
        return ResponseEntity.ok("Product removed from cart");
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared");
    }

    // New endpoints for saved items
    @GetMapping("/{userId}/saved")
    public ResponseEntity<List<Product>> getSavedItems(@PathVariable String userId) {
        return ResponseEntity.ok(savedItemsService.getSavedItems(userId));
    }

    @PostMapping("/save-for-later")
    public ResponseEntity<?> saveForLater(@RequestParam String userId, @RequestParam String productId) {
        savedItemsService.saveForLater(userId, productId);
        return ResponseEntity.ok("Product saved for later");
    }

    @PostMapping("/move-to-cart")
    public ResponseEntity<?> moveToCart(@RequestParam String userId, @RequestParam String productId) {
        savedItemsService.moveToCart(userId, productId);
        return ResponseEntity.ok("Product moved to cart");
    }

    @DeleteMapping("/saved/remove")
    public ResponseEntity<?> removeSavedItem(@RequestParam String userId, @RequestParam String productId) {
        savedItemsService.removeSavedItem(userId, productId);
        return ResponseEntity.ok("Saved item removed");
    }

    @DeleteMapping("/saved/clear/{userId}")
    public ResponseEntity<?> clearSavedItems(@PathVariable String userId) {
        savedItemsService.clearSavedItems(userId);
        return ResponseEntity.ok("Saved items cleared");
    }

    // Checkout endpoint
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<?> checkout(@PathVariable String userId, @RequestBody Map<String, Object> checkoutDetails) {
        try {
            Map<String, Object> result = checkoutService.checkout(userId, checkoutDetails);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Checkout failed: " + e.getMessage());
        }
    }
}