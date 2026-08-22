package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.request.WishlistRequest;
import com.dreamcart.backend.dto.response.WishlistResponse;
import com.dreamcart.backend.entity.Product;
import com.dreamcart.backend.entity.User;
import com.dreamcart.backend.entity.Wishlist;
import com.dreamcart.backend.exceptions.ResourceNotFoundException;
import com.dreamcart.backend.repository.ProductRepository;
import com.dreamcart.backend.repository.UserRepository;
import com.dreamcart.backend.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
     * Adds a product to the authenticated user's wishlist and returns the updated list.
     */
    public List<WishlistResponse> addToWishlist(String email, WishlistRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (wishlistRepository.findByUserAndProduct(user, product).isEmpty()) {
            Wishlist wishlist = Wishlist.builder()
                    .user(user)
                    .product(product)
                    .build();
            wishlistRepository.save(wishlist);
        }

        return getWishlist(email);
    }

    /*
     * Returns all wishlist items of the authenticated user mapped to DTOs.
     */
    public List<WishlistResponse> getWishlist(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Wishlist> wishlists = wishlistRepository.findByUser(user);

        return wishlists.stream().map(this::mapToWishlistResponse).collect(Collectors.toList());
    }

    /*
     * Removes a product from the authenticated user's wishlist.
     */
    public List<WishlistResponse> removeFromWishlist(String email, Long productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Wishlist wishlist = wishlistRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist item not found"));

        wishlistRepository.delete(wishlist);

        return getWishlist(email);
    }

    /*
     * Helper method to map entity to response DTO
     */
    private WishlistResponse mapToWishlistResponse(Wishlist wishlist) {
        return WishlistResponse.builder()
                .id(wishlist.getId())
                .productId(wishlist.getProduct().getId())
                .productName(wishlist.getProduct().getName())
                .imageUrl(wishlist.getProduct().getImageUrl())
                .price(wishlist.getProduct().getPrice())
                .stockQuantity(wishlist.getProduct().getStockQuantity())
                .build();
    }
}