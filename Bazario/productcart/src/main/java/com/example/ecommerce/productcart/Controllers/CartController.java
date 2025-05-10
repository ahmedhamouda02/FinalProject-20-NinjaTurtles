package com.example.ecommerce.productcart.Controllers;

import com.example.ecommerce.productcart.Models.Cart;
import com.example.ecommerce.productcart.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @GetMapping("/{userId}")
    public List<Cart> getCartByUser(@PathVariable String userId) {
        return cartService.getCartByUser(userId);
    }
    @PostMapping("/add")
    public Cart addToCart(@RequestBody Cart cart) {
        return cartService.addToCart(cart);
    }
    @DeleteMapping("/remove")
    public void removeFromCart(@RequestParam String userId, @RequestParam String productId){
        cartService.removeFromCart(userId, productId);
    }
    @DeleteMapping("/clear/{userId}")
    public void clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
    }
}
