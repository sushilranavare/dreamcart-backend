package com.dreamcart.backend.repository;

import com.dreamcart.backend.entity.Order;
import com.dreamcart.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}