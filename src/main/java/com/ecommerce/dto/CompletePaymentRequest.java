package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletePaymentRequest {
    
    @NotBlank(message = "External transaction ID is required")
    private String externalTransactionId;
}

// Made with Bob
