/*
* Repository interface for Product category
* This repository will used to product related database operations
* */
package com.dreamcart.backend.repository;
import com.dreamcart.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    /*
     * Checks whether any product is assigned to the given category id.
     * Useful before deleting a category.
     */
    boolean existsByCategoryId(Long categoryId);

    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseAndCategory_Id(String keyword, Long categoryId, Pageable pageable);


}
