/*
* This controller exposes REST APIs for managing product reviews and ratings.
* */
package com.dreamcart.backend.controller;
import com.dreamcart.backend.dto.request.ReviewRequest;
import com.dreamcart.backend.dto.response.ReviewResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.dreamcart.backend.service.ReviewService;

import javax.sound.sampled.ReverbType;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    /*
    * Add a review for product.
    * Accessible by any logged-in user (USER or ADMIN).
    * */

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ReviewResponse addReview(
            Authentication authentication,
            @Valid @RequestBody ReviewRequest request){

        return reviewService.addReview(authentication.getName(), request);
    }

    /* Get all reviews for a product publicly accessible, just like product and category browsing.
    * */
    @GetMapping("/product/{productId}")
    public List<ReviewResponse> getReviewsForProduct(@PathVariable Long productId){
        return reviewService.getReviewsForProduct(productId);
    }

    /* Update Reviews and ratings */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}")
    public ReviewResponse updateReview(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody ReviewRequest request) {

        return reviewService.updateReview(id, authentication.getName(), request);
    }

    /*Delete a review. Accessible only by the reviews own auther.*/

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            Authentication authentication){

        reviewService.deleteReview(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

}
