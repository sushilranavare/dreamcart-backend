/*
* This dashboard represents the statistics displayed on the administrator dashboard.
* */

package com.dreamcart.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {

    /* Total registered users. */
    private Long totalUsers;

    /* Total products */
    private Long totalProducts;

    /* Total categories*/
    private Long totalCategories;

    /* Total orders */
    private Long totalOrders;

    /* Total confirmed revenue */
    private Long confirmedOrders;

    /* Total placed revenue */
    private Long placedOrders;

    /* total revenue */
    BigDecimal totalRevenue;

    /* Total reviews */
    private Long totalReviews;

    /* Total wishlist items */
    private Long totalWishlistItems;



}
