package com.ecommerce.dto;

import com.ecommerce.entity.TransactionType;
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
public class GiftPointTransactionDTO {
    private Long transactionId;
    private Long accountId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String description;
    private LocalDateTime transactionDate;
}

// Made with Bob
