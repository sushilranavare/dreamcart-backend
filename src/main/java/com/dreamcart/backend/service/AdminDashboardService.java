/* This service calculates statistics for the   DreamCart administrator dashboard. */

package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.response.AdminDashboardResponse;
import com.dreamcart.backend.repository.CategoryRepository;
import com.dreamcart.backend.repository.ProductRepository;
import com.dreamcart.backend.repository.OrderRepository;
import com.dreamcart.backend.repository.UserRepository;
import com.dreamcart.backend.repository.WishlistRepository;
import com.dreamcart.backend.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.math.BigDecimal;

@Service
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final WishlistRepository wishlistRepository;

    public AdminDashboardService(
            UserRepository  userRepository,
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            ReviewRepository reviewRepository,
            WishlistRepository wishlistRepository
        )
    {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.wishlistRepository = wishlistRepository;
        this.reviewRepository = reviewRepository;
    }

    /* Returns statistics for the administrator dashboard */

    public AdminDashboardResponse getDashboardStatistics() {
        BigDecimal totalRevenue =
                orderRepository.getTotalRevenue();

        return  AdminDashboardResponse.builder()
                .totalUsers(userRepository.count())
                .totalProducts(productRepository.count())
                .totalCategories(categoryRepository.count())
                .totalOrders(orderRepository.count())
                .totalReviews(reviewRepository.count())
                .totalWishlistItems(wishlistRepository.count())
                .totalRevenue(totalRevenue)
                .placedOrders(orderRepository.countByStatus("PLACED"))
                .totalOrders(orderRepository.countByStatus("CONFIRMED"))
                .build();
    }


}
