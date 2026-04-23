/*
 * This service contains the business logic related to categories.
 * It acts as a bridge between the controller layer and repository layer.
 */
package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.request.CreateCategoryRequest;
import com.dreamcart.backend.dto.request.UpdateCategoryRequest;
import com.dreamcart.backend.dto.response.CategoryResponse;
import com.dreamcart.backend.entity.Category;
import com.dreamcart.backend.exceptions.ResourceNotFoundException;
import com.dreamcart.backend.repository.CategoryRepository;
import com.dreamcart.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }
    /*
     * Fetches all categories from the database and converts them into response DTOs.
     */
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToCategoryResponse)
                .toList();
    }
    /*
     * Creates a new category after checking duplicate category names.
     */
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Category already exists with name: " + request.getName());
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(savedCategory);
    }
    /*
     * Updates an existing category after validating that the new name
     * does not conflict with another category.
     */
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        Optional<Category> existingCategory = categoryRepository.findByName(request.getName());
        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(id)) {
            throw new RuntimeException("Category already exists with name: " + request.getName());
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(updatedCategory);
    }
    /*
     * Deletes a category only if no products are assigned to it.
     */
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (productRepository.existsByCategoryId(id)) {
            throw new RuntimeException("Cannot delete category because products are assigned to it");
        }

        categoryRepository.delete(category);
    }
    /*
     * Converts Category entity data into a CategoryResponse DTO.
     */
    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}