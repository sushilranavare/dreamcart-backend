/*
* Repository interface for Product category
* This repository will used to product related database operations
* */
package com.dreamcart.backend.repository;
import com.dreamcart.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
}
