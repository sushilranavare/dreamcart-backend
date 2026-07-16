package com.dreamcart.backend.repository;

import com.dreamcart.backend.entity.Order;
import com.dreamcart.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /*Returns all orders belonging to specific user. */
    List<Order> findByUser(User user);

    /* Counts order by their status */
    long countByStatus(String status);

    /* Calculate total revenue. */
    @Query("""  
    SELECT COALESCE (SUM(o.totalAmount),0)
    FROM  Order o
    WHERE o.status = 'CONFIRMED' 
        """)

    BigDecimal getTotalRevenue();
}