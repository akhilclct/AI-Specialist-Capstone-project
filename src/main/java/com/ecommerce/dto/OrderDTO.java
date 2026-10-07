package com.ecommerce.dto;

import com.ecommerce.entity.OrderStatus;
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
public class OrderDTO {
    
    private Long orderId;
    private Long userId;
    private Long deliveryAddressId;
    private AddressDTO deliveryAddress;
    private OrderStatus orderStatus;
    private BigDecimal subtotal;
    private BigDecimal giftPointsRedeemed;
    private BigDecimal totalAmount;
    private String currencyCode;
    private LocalDateTime orderedAt;
    private List<OrderItemDTO> items;
}

// Made with Bob
