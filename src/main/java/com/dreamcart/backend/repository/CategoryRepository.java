/*
Repository interface for category entity.
This interface is used to manage catefory data in the database.
also used for product organization and category based filtering.
* */
package com.dreamcart.backend.repository;
import com.dreamcart.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);//Used to find a category by its name
}
