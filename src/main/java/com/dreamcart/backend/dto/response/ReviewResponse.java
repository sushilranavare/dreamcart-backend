/*
* This DTO represents a single product review as returned to the client.
* It exposes only the fields the frontend needs, instead of the raw Review entity,
* so the reviewer's full User record is never leaked.
* */

package com.dreamcart.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class ReviewResponse {
    private Long id;

    private Long productId;

    private Long userId;

    private String reviewerName;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;
}
