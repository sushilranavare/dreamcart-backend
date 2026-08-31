/*
 * This service contains the business logic for managing
 * product reviews and ratings.
 */
package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.request.ReviewRequest;
import com.dreamcart.backend.dto.response.ReviewResponse;
import com.dreamcart.backend.entity.Product;
import com.dreamcart.backend.entity.Review;
import com.dreamcart.backend.entity.User;
import com.dreamcart.backend.exceptions.ResourceNotFoundException;
import com.dreamcart.backend.repository.ProductRepository;
import com.dreamcart.backend.repository.ReviewRepository;
import com.dreamcart.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         UserRepository userRepository,
                         ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /*
     * Adds a review for a product from the authenticated user.
     * A user can only leave one review per product; they must
     * edit their existing review instead of adding a second one.
     */
    public ReviewResponse addReview(String email, ReviewRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (reviewRepository.findByUserAndProduct(user, product).isPresent()) {
            throw new RuntimeException(
                    "You have already reviewed this product. Edit your existing review instead.");
        }

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);

        return mapToReviewResponse(review);
    }

    /*
     * Returns all reviews for a product, newest first.
     */
    public List<ReviewResponse> getReviewsForProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return reviewRepository.findByProduct(product).stream()
                // nullsLast() guards against any legacy row saved without a
                // createdAt value, so one bad row can't crash the whole list
                .sorted(Comparator.comparing(Review::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    /*
     * Updates the rating/comment of a review. Only the review's
     * own author is allowed to edit it.
     */
    public ReviewResponse updateReview(Long id, String email, ReviewRequest request) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to edit this review.");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        return mapToReviewResponse(review);
    }

    /*
     * Deletes a review. Only the review's own author is allowed
     * to delete it.
     */
    public void deleteReview(Long id, String email) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to delete this review.");
        }

        reviewRepository.delete(review);
    }

    /*
     * Helper method to map entity to response DTO
     */
    private ReviewResponse mapToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .userId(review.getUser().getId())
                .reviewerName(review.getUser().getFirstName() + " " + review.getUser().getLastName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}