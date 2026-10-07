package com.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    
    @NotNull(message = "Delivery address ID is required")
    private Long deliveryAddressId;
    
    @PositiveOrZero(message = "Gift points to redeem must be zero or positive")
    private BigDecimal giftPointsToRedeem;
}

// Made with Bob
