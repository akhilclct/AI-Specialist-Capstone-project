package com.ecommerce.dto;

import com.ecommerce.entity.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    
    private Long cartId;
    private Long userId;
    private CartStatus status;
    private List<CartItemDTO> items;
    private BigDecimal subtotal;
    private Integer itemCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// Made with Bob
