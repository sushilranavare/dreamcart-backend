/*
* Repository interface for Product category
* This repository will used to product related database operations
* */
package com.dreamcart.backend.repository;
import com.dreamcart.backend.entity.Category;
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

    /*Seach products by product name */
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    /*Search product by Category ID*/
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    /* Get all products in a category*/
    Page<Product>findByCategory(Category category, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndCategory_Id(String keyword, Long categoryId, Pageable pageable);

    /*Filter products by price range*/
    Page<Product> findByPriceBetween(
            java.math.BigDecimal min,
            java.math.BigDecimal max,
            Pageable pageable
    );

    /*Returns total number of registered prodcuts*/

    @Override
    long count();
}
