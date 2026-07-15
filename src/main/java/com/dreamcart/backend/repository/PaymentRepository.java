package com.dreamcart.backend.repository;

import com.dreamcart.backend.entity.Order;
import com.dreamcart.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

    Optional<Payment> findByOrder(Order order);
}
