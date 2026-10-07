package com.ecommerce.repository;

import com.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);
    
    Page<Product> findByBrandBrandId(Long brandId, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE " +
           "(:categoryId IS NULL OR p.category.categoryId = :categoryId) AND " +
           "(:brandId IS NULL OR p.brand.brandId = :brandId) AND " +
           "(CAST(:search AS string) IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))")
    Page<Product> searchProducts(
        @Param("categoryId") Long categoryId,
        @Param("brandId") Long brandId,
        @Param("search") String search,
        Pageable pageable
    );

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId AND p.productId != :productId ORDER BY RANDOM()")
    List<Product> findRelatedProducts(@Param("categoryId") Long categoryId, @Param("productId") Long productId, Pageable pageable);
}

// Made with Bob
