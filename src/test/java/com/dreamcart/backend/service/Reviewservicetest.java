/*
 * Unit tests for ReviewService: one review per user per product,
 * and edit/delete restricted to the review's own author.
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {

        user = User.builder().id(1L).email("jane@example.com").firstName("Jane").lastName("Doe").build();
        product = Product.builder().id(10L).name("Galaxy A17").build();
    }

    @Test
    void addReview_savesReviewWhenUserHasNotReviewedYet() {

        ReviewRequest request = new ReviewRequest();
        request.setProductId(10L);
        request.setRating(5);
        request.setComment("Great phone!");

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(reviewRepository.findByUserAndProduct(user, product)).thenReturn(Optional.empty());
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewResponse response = reviewService.addReview("jane@example.com", request);

        assertEquals(5, response.getRating());
        assertEquals("Great phone!", response.getComment());
        assertEquals("Jane Doe", response.getReviewerName());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void addReview_throwsWhenUserAlreadyReviewedThisProduct() {

        ReviewRequest request = new ReviewRequest();
        request.setProductId(10L);
        request.setRating(4);
        request.setComment("Second attempt");

        Review existingReview = Review.builder().id(99L).user(user).product(product).rating(5).build();

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(reviewRepository.findByUserAndProduct(user, product)).thenReturn(Optional.of(existingReview));

        assertThrows(RuntimeException.class,
                () -> reviewService.addReview("jane@example.com", request));

        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void updateReview_allowsTheReviewsOwnAuthorToEditIt() {

        Review review = Review.builder().id(5L).user(user).product(product).rating(3).comment("okay").build();

        ReviewRequest request = new ReviewRequest();
        request.setProductId(10L);
        request.setRating(5);
        request.setComment("Actually it's great");

        when(reviewRepository.findById(5L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewResponse response = reviewService.updateReview(5L, "jane@example.com", request);

        assertEquals(5, response.getRating());
        assertEquals("Actually it's great", response.getComment());
    }

    @Test
    void updateReview_throwsWhenEditedByAnotherUser() {

        Review review = Review.builder().id(5L).user(user).product(product).rating(3).comment("okay").build();

        ReviewRequest request = new ReviewRequest();
        request.setProductId(10L);
        request.setRating(1);
        request.setComment("trying to hijack this review");

        when(reviewRepository.findById(5L)).thenReturn(Optional.of(review));

        assertThrows(RuntimeException.class,
                () -> reviewService.updateReview(5L, "someone-else@example.com", request));

        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void deleteReview_throwsWhenDeletedByAnotherUser() {

        Review review = Review.builder().id(5L).user(user).product(product).rating(3).build();

        when(reviewRepository.findById(5L)).thenReturn(Optional.of(review));

        assertThrows(RuntimeException.class,
                () -> reviewService.deleteReview(5L, "someone-else@example.com"));

        verify(reviewRepository, never()).delete(any(Review.class));
    }

    @Test
    void getReviewsForProduct_sortsNewestFirstAndSurvivesMissingCreatedAt() {

        Review reviewWithNullDate = Review.builder()
                .id(1L).user(user).product(product).rating(3).comment("legacy row").createdAt(null).build();

        Review olderReview = Review.builder()
                .id(2L).user(user).product(product).rating(4).comment("older")
                .createdAt(LocalDateTime.now().minusDays(1)).build();

        Review newestReview = Review.builder()
                .id(3L).user(user).product(product).rating(5).comment("newest")
                .createdAt(LocalDateTime.now()).build();

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(reviewRepository.findByProduct(product))
                .thenReturn(List.of(reviewWithNullDate, olderReview, newestReview));

        List<ReviewResponse> responses = reviewService.getReviewsForProduct(10L);

        assertEquals(3, responses.size());
        assertEquals("newest", responses.get(0).getComment());
        assertEquals("older", responses.get(1).getComment());
        assertEquals("legacy row", responses.get(2).getComment());
    }

    @Test
    void getReviewsForProduct_throwsWhenProductNotFound() {

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> reviewService.getReviewsForProduct(999L));
    }
}