/*
 * This service handles all wishlist-related business logic.
 * It allows authenticated users to add products to their wishlist,
 * view their wishlist, and remove products from it.
 */
package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.request.WishlistRequest;
import com.dreamcart.backend.entity.Product;
import com.dreamcart.backend.entity.User;
import com.dreamcart.backend.entity.Wishlist;
import com.dreamcart.backend.repository.ProductRepository;
import com.dreamcart.backend.repository.UserRepository;
import com.dreamcart.backend.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /*
     * Adds a product to the authenticated user's wishlist.
     */
    public Wishlist addToWishlist(String email, WishlistRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (wishlistRepository.findByUserAndProduct(user, product).isPresent()) {
            throw new RuntimeException("Product already exists in wishlist");
        }

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();

        return wishlistRepository.save(wishlist);
    }

    /*
     * Returns all wishlist items of the authenticated user.
     */
    public List<Wishlist> getWishlist(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return wishlistRepository.findByUser(user);
    }

    /*
     * Removes a product from the authenticated user's wishlist.
     */
    public void removeFromWishlist(String email, Long productId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = wishlistRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found"));

        wishlistRepository.delete(wishlist);
    }
}