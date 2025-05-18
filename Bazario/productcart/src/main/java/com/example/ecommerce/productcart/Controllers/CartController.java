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

    @GetMapping("/{userId}")
    public ResponseEntity<List<Product>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam Long userId, @RequestParam String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartService.addToCart(userId, product);
        return ResponseEntity.ok("Product added to cart");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeProduct(@RequestParam Long userId, @RequestParam String productId) {
        cartService.removeProduct(userId, productId);
        return ResponseEntity.ok("Product removed from cart");
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared");
    }

    // New endpoints for saved items
    @GetMapping("/{userId}/saved")
    public ResponseEntity<List<Product>> getSavedItems(@PathVariable Long userId) {
        return ResponseEntity.ok(savedItemsService.getSavedItems(userId));
    }

    @PostMapping("/save-for-later/{userId}")
    public ResponseEntity<String> saveCartForLater(@PathVariable Long userId) {
        String message = cartService.saveCartForLater(userId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/move-to-cart")
    public ResponseEntity<?> moveToCart(@RequestParam Long userId, @RequestParam String productId) {
        savedItemsService.moveToCart(userId, productId);
        return ResponseEntity.ok("Product moved to cart");
    }

    @DeleteMapping("/saved/remove")
    public ResponseEntity<?> removeSavedItem(@RequestParam Long userId, @RequestParam String productId) {
        savedItemsService.removeSavedItem(userId, productId);
        return ResponseEntity.ok("Saved item removed");
    }

    @DeleteMapping("/saved/clear/{userId}")
    public ResponseEntity<?> clearSavedItems(@PathVariable Long userId) {
        savedItemsService.clearSavedItems(userId);
        return ResponseEntity.ok("Saved items cleared");
    }

    // // Checkout endpoint
    // @PostMapping("/{userId}/checkout")
    // public ResponseEntity<?> checkout(@PathVariable Long userId, @RequestBody Map<String, Object> checkoutDetails) {
    //     try {
    //         Map<String, Object> result = checkoutService.checkout(userId, checkoutDetails);
    //         return ResponseEntity.ok(result);
    //     } catch (IllegalStateException e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     } catch (Exception e) {
    //         return ResponseEntity.internalServerError().body("Checkout failed: " + e.getMessage());
    //     }
    // }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<?> checkoutCart(@PathVariable Long userId) {
        try {
            double total = cartService.checkoutCart(userId);
            return ResponseEntity.ok(Map.of(
                    "userId", userId,
                    "totalPrice", total
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Process checkout with payment options
     */
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<?> checkout(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "NO_DISCOUNT") String discountCode,
            @RequestParam(defaultValue = "CREDIT_CARD") String paymentMethod) {
        try {
            Map<String, Object> result = cartService.checkout(userId, discountCode, paymentMethod);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
