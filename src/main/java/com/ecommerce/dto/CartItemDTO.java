package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    
    private Long cartItemId;
    private Long cartId;
    private Long productId;
    private ProductDTO product;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}

// Made with Bob
