package com.ecommerce.controller;

import com.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.dto.OrderDTO;
import com.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order management operations")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getUserOrders(
            Authentication authentication,
            @PageableDefault(size = 20, sort = "orderedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(orderService.getUserOrders(userId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(orderService.getOrderById(userId, id));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderRequest request,
                                               Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderDTO order = orderService.createOrder(
                userId,
                request.getDeliveryAddressId(),
                request.getGiftPointsToRedeem()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/{id}/buy-again")
    public ResponseEntity<Void> buyAgain(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        orderService.buyAgain(userId, id);
        return ResponseEntity.ok().build();
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }
}

// Made with Bob
