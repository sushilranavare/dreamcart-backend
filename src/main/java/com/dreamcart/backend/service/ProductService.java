/*
 * This service contains the core business logic for product management.
 * It handles CRUD operations as well as pagination, filtering, sorting, and search.
 */
package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.request.CreateProductRequest;
import com.dreamcart.backend.dto.request.UpdateProductRequest;
import com.dreamcart.backend.dto.response.ProductPageResponse;
import com.dreamcart.backend.dto.response.ProductResponse;
import com.dreamcart.backend.entity.Category;
import com.dreamcart.backend.entity.Product;
import com.dreamcart.backend.exceptions.ResourceNotFoundException;
import com.dreamcart.backend.repository.CategoryRepository;
import com.dreamcart.backend.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.math.BigDecimal;


import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageStorageService imageStorageService;


    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ImageStorageService imageStorageService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.imageStorageService = imageStorageService;
    }
    /*
     * Returns a paginated list of products based on optional search, filter,
     * sort, and pagination parameters.
     */
    public ProductPageResponse getAllProducts(int page,
                                              int size,
                                              String sortBy,
                                              String sortDir,
                                              String keyword,
                                              Long categoryId) {

        Set<String> allowedSortFields = Set.of("id", "name", "price", "stockQuantity");
        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "id";
        }



        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        String cleanedKeyword = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();

        Page<Product> productPage;

        if (cleanedKeyword != null && categoryId != null) {
            productPage = productRepository.findByNameContainingIgnoreCaseAndCategory_Id(cleanedKeyword, categoryId, pageable);
        } else if (cleanedKeyword != null) {
            productPage = productRepository.findByNameContainingIgnoreCase(cleanedKeyword, pageable);
        } else if (categoryId != null) {
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<ProductResponse> productResponses = productPage.getContent()
                .stream()
                .map(this::mapToProductResponse)
                .toList();

        return ProductPageResponse.builder()
                .content(productResponses)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();
    }
    /*
     * Fetches a single product by id.
     * Throws an exception if the product does not exist.
     */
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return mapToProductResponse(product);
    }
    /*
     * Creates a new product and links it to the selected category.
     */
    public ProductResponse createProduct(CreateProductRequest request, MultipartFile image) throws IOException {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));


        String imageUrl = imageStorageService.saveImage(image);

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(imageUrl);
        product.setIsActive(true);
        product.setCategory(category);


        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }
    /*
     * Updates an existing product using new request data.
     */

    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setIsActive(request.getIsActive());
        product.setCategory(category);


        Product updatedProduct = productRepository.save(product);
        return mapToProductResponse(updatedProduct);
    }
    /*
     * Deletes a product by id.
     */
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
    }
    /*
     * Converts Product entity data into ProductResponse DTO
     * so the API returns only clean and required fields.
     */
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .isActive(product.getIsActive())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .build();
    }
}