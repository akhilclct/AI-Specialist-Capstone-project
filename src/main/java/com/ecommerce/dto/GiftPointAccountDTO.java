package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftPointAccountDTO {
    
    private Long giftPointAccountId;
    private Long userId;
    private BigDecimal balance;
    private LocalDateTime updatedAt;
}

// Made with Bob
