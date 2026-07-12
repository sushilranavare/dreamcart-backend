package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.request.AddToCartRequest;
import com.dreamcart.backend.entity.Cart;
import com.dreamcart.backend.entity.CartItem;
import com.dreamcart.backend.entity.Product;
import com.dreamcart.backend.entity.User;
import com.dreamcart.backend.repository.CartItemRepository;
import com.dreamcart.backend.repository.CartRepository;
import com.dreamcart.backend.repository.ProductRepository;
import com.dreamcart.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * Add product to cart.
     */
    public CartItem addToCart(String email, AddToCartRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(
                    cartItem.getQuantity() + request.getQuantity()
            );
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
        }

        return cartItemRepository.save(cartItem);
    }

    /**
     * View all cart items.
     */
    public List<CartItem> getCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        return cartItemRepository.findByCart(cart);
    }

    /**
     * Remove item from cart.
     */
    public void removeItem(Long cartItemId) {

        if (!cartItemRepository.existsById(cartItemId)) {
            throw new RuntimeException("Cart item not found");
        }

        cartItemRepository.deleteById(cartItemId);
    }

    /**
     * Clear cart.
     */
    public void clearCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        cartItemRepository.deleteByCart(cart);
    }
}