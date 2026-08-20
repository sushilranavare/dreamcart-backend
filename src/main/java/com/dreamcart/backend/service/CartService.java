package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.request.AddToCartRequest;
import com.dreamcart.backend.dto.request.UpdateCartItemRequest;
import com.dreamcart.backend.dto.response.CartItemResponse;
import com.dreamcart.backend.dto.response.CartResponse;
import com.dreamcart.backend.entity.Cart;
import com.dreamcart.backend.entity.CartItem;
import com.dreamcart.backend.entity.Product;
import com.dreamcart.backend.entity.User;
import com.dreamcart.backend.exceptions.ResourceNotFoundException;
import com.dreamcart.backend.repository.CartItemRepository;
import com.dreamcart.backend.repository.CartRepository;
import com.dreamcart.backend.repository.ProductRepository;
import com.dreamcart.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
            UserRepository userRepository
            ) {

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
    public CartResponse getCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        List<CartItem> items = cartItemRepository.findByCart(cart);
        return mapToCartResponse(cart,items);

    }

    // Update quantity of Cart items.

    public CartResponse updateQuantity(String email, Long cartItemId, UpdateCartItemRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this cart item");
        }
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        return (CartResponse) getCart(email);
    }

    /**
     * Remove item from cart.
     */
    public CartResponse removeItem(String email, Long cartItemId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to remove this cart item");
        }

        cartItemRepository.delete(cartItem);
        return (CartResponse) getCart(email);
    }

    /**
     * Clear cart.
     */
    @Transactional
    public CartResponse clearCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cartItemRepository.deleteByCart(cart);

        return (CartResponse) getCart(email);
    }

    //Maps Cart entity and items to CartResponse DTO.

    private CartResponse mapToCartResponse (Cart cart, List<CartItem> items){
        BigDecimal total = BigDecimal.ZERO;

        List<CartItemResponse> itemResponses = items.stream().map(item ->{
            BigDecimal subtotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            return CartItemResponse.builder()
                    .id(item.getId())
                    .productId(item.getProduct().getId())
                    .productName(item.getProduct().getName())
                    .imageUrl(item.getProduct().getImageUrl())
                    .price(item.getProduct().getPrice())
                    .quantity(item.getQuantity())
                    .subtotal(subtotal)
                    .build();
        }) .collect(Collectors.toList());

        for(CartItemResponse itemResponse : itemResponses){
            total = total.add(itemResponse.getSubtotal());
        }
        return CartResponse.builder()
                .cartId(cart.getId())
                .items(itemResponses)
                .total(total)
                .build();
    }
}