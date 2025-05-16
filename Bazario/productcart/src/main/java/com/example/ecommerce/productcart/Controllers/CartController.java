package com.example.ecommerce.productcart.Controllers;

import com.example.ecommerce.productcart.Models.Cart;
import com.example.ecommerce.productcart.Models.Product;
import com.example.ecommerce.productcart.Services.CartService;
import com.example.ecommerce.productcart.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;
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
}
