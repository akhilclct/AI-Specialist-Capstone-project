package com.ecommerce.dto;

import com.ecommerce.entity.PaymentMethod;
import com.ecommerce.entity.PaymentStatus;
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
public class PaymentDTO {
    
    private Long paymentId;
    private Long orderId;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private String currencyCode;
    private PaymentStatus paymentStatus;
    private String transactionReference;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;
}

// Made with Bob
