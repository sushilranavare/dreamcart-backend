package com.dreamcart.backend.controller;
import com.dreamcart.backend.dto.request.CreateCategoryRequest;
import com.dreamcart.backend.dto.response.CategoryResponse;
import com.dreamcart.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;

    }
    @GetMapping
    public List<CategoryResponse> getAllCategories(){
        return  getAllCategories();
    }
    @PostMapping
    public CategoryResponse createCategory(@Valid @RequestBody CreateCategoryRequest request){
        return categoryService.createCategory(request);
    }
}
