package com.ecommerce.controller;

import com.ecommerce.dto.CategoryDTO;
import com.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Product category operations")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "List all categories", description = "Get all product categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long categoryId) {
        CategoryDTO category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }
}

// Made with Bob
