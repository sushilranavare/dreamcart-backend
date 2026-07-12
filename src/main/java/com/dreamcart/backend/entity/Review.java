/*
Entity representing a product review and rating.
*
* */
package com.dreamcart.backend.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    // User who wrote review.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Product being reviewed
    @ManyToOne
    @JoinColumn(name = "Product_id", nullable = false)
    private Product product;

    //Rating between 1 and 5.

    @Column(nullable = false)
    private Integer rating;

    //Review Comment
    @Column (length = 1000)
    private String comment;

    // Review creation timestamp
    private LocalDateTime createdAt;

}
