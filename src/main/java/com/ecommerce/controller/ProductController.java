package com.ecommerce.controller;

import com.ecommerce.dto.ProductDTO;
import com.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product catalogue and details")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "List products", description = "Get paginated list of products with optional filters")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> products = productService.getAllProducts(categoryId, brandId, search, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product details", description = "Retrieve detailed information about a specific product")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        ProductDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{productId}/related")
    @Operation(summary = "Get related products", description = "Get products related to the specified product")
    public ResponseEntity<List<ProductDTO>> getRelatedProducts(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "5") int limit) {
        
        List<ProductDTO> relatedProducts = productService.getRelatedProducts(productId, limit);
        return ResponseEntity.ok(relatedProducts);
    }
}

// Made with Bob
