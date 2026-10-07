package com.ecommerce.service;

import com.ecommerce.dto.ProductDTO;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductDTO> getAllProducts(Long categoryId, Long brandId, String search, Pageable pageable) {
        Page<Product> products;
        
        if (categoryId != null || brandId != null || search != null) {
            products = productRepository.searchProducts(categoryId, brandId, search, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }
        
        return products.map(this::toDTO);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return toDTO(product);
    }

    public List<ProductDTO> getRelatedProducts(Long productId, int limit) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        
        List<Product> relatedProducts = productRepository.findRelatedProducts(
                product.getCategory().getCategoryId(), 
                productId, 
                Pageable.ofSize(limit)
        );
        
        return relatedProducts.stream().map(this::toDTO).toList();
    }

    private ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .productId(product.getProductId())
                .categoryId(product.getCategory().getCategoryId())
                .brandId(product.getBrand().getBrandId())
                .name(product.getName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .currencyCode(product.getCurrencyCode())
                .stockQuantity(product.getStockQuantity())
                .thumbnailUrl(product.getThumbnailUrl())
                .build();
    }
}

// Made with Bob
