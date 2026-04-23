/*
 * This controller handles all category-related API requests.
 * It receives HTTP requests from the client and delegates the actual business logic
 * to the CategoryService layer.
 */
package com.dreamcart.backend.controller;

import com.dreamcart.backend.dto.request.CreateCategoryRequest;
import com.dreamcart.backend.dto.request.UpdateCategoryRequest;
import com.dreamcart.backend.dto.response.CategoryResponse;
import com.dreamcart.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    /*
     * Returns all available categories.
     */
    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }
    /*
     * Creates a new category after validating the request data.
     */
    @PostMapping
    public CategoryResponse createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }
    /*
     * Updates an existing category using its id.
     */
    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id,
                                           @Valid @RequestBody UpdateCategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }
    /*
     * Deletes a category if it is allowed by business rules.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}