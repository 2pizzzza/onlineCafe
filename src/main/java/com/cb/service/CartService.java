package com.cb.service;

import com.cb.model.Cart;
import com.cb.model.MenuItem;
import com.cb.model.User;
import com.cb.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    public void addItemToCart(Cart cart, MenuItem menuItem) {
        cart.getItems().add(menuItem);
        cartRepository.save(cart);
    }

    public void removeItemFromCart(Cart cart, MenuItem menuItem) {
        cart.getItems().remove(menuItem);
        cartRepository.save(cart);
    }
    public void clearCart(Cart cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
