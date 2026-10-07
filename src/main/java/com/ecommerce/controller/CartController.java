package com.ecommerce.controller;

import com.ecommerce.dto.CartDTO;
import com.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Shopping cart operations")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "Get current cart", description = "Retrieve the authenticated user's active shopping cart")
    public ResponseEntity<CartDTO> getCart(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        CartDTO cart = cartService.getActiveCart(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to cart", description = "Add a product to the shopping cart")
    public ResponseEntity<CartDTO> addItemToCart(
            @Valid @RequestBody AddCartItemRequest request,
            Authentication authentication) {
        
        Long userId = getUserIdFromAuth(authentication);
        CartDTO cart = cartService.addItemToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update cart item", description = "Update the quantity of a cart item")
    public ResponseEntity<CartDTO> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request,
            Authentication authentication) {
        
        Long userId = getUserIdFromAuth(authentication);
        CartDTO cart = cartService.updateCartItem(userId, cartItemId, request.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Remove cart item", description = "Remove an item from the shopping cart")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable Long cartItemId,
            Authentication authentication) {
        
        Long userId = getUserIdFromAuth(authentication);
        cartService.removeCartItem(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear cart", description = "Remove all items from the shopping cart")
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddCartItemRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;
        
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCartItemRequest {
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
    }
}

// Made with Bob
