package com.dreamcart.backend.repository;

import com.dreamcart.backend.entity.Product;
import com.dreamcart.backend.entity.Review;
import com.dreamcart.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository  extends JpaRepository<Review, Long>{
    List<Review> findByProduct(Product product);

    Optional<Review> findByUserAndProduct(User user, Product product);

}
