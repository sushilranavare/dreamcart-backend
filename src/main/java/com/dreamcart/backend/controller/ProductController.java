/*
 * This controller handles all product-related API endpoints.
 * It supports product creation, retrieval, update, deletion,
 * and product listing with pagination, filtering, searching, and sorting.
 */
package com.dreamcart.backend.controller;

import com.dreamcart.backend.dto.request.CreateProductRequest;
import com.dreamcart.backend.dto.request.UpdateProductRequest;
import com.dreamcart.backend.dto.response.ProductPageResponse;
import com.dreamcart.backend.dto.response.ProductResponse;
import com.dreamcart.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    /*
     * Returns a paginated list of products.
     * Optional query parameters can be used for search, filter, and sorting.
     * Accessible by ADMIN and USER
     */

    @GetMapping
    public ProductPageResponse getAllProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Long categoryId
    )
    {

        return productService.getAllProducts(page, size, sortBy, sortDir, keyword, categoryId);
    }
    /*
     * Returns a single product by its id.
     * Accessible by ADMIN and Users
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    /*
     * Creates a new product from validated request data.
     * Accessible only by ADMIN users
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = "multipart/form-data")
    public ProductResponse createProduct(@Valid @RequestPart("product") CreateProductRequest request,
                                         @RequestPart("image") MultipartFile image) throws IOException {
        return productService.createProduct(request, image);
    }
    /*
     * Updates an existing product by id.
     * Accessible only by ADMIN
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id,
                                         @Valid @RequestBody UpdateProductRequest request) {
        return productService.updateProduct(id, request);
    }
    /*
     * Deletes a product by id.
     * Accessible ony by ADMIN
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}