package com.example.ecommerce.productcart.Services;

import com.example.ecommerce.productcart.Models.Cart;
import com.example.ecommerce.productcart.Repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public List<Cart> getCartByUser(String userId) {
        return cartRepository.findByUserId(userId);
    }
    public Cart addToCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public void removeFromCart(String userId,String productId){
        cartRepository.deleteByProductIdAndUserId(userId,productId);
    }
    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
    public Cart findByProductIdAndUserId(String productId, String userId) {
        return cartRepository.findByProductIdAndUserId(productId, userId);
    }



}
