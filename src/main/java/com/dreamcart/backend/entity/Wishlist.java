/*Entity representing a user's wishlist.
* Each wishlist entry links one user with one product.*/

package com.dreamcart.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "wishlist",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id","product_id"})
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlist {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Owner if the wishlist item.

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    //Product saved by the user.
    @ManyToOne
    @JoinColumn(name = "Product_id", nullable = false)
    private Product product;
}
