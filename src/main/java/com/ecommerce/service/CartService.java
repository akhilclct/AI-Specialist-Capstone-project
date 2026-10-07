package com.ecommerce.service;

import com.ecommerce.dto.CartDTO;
import com.ecommerce.dto.CartItemDTO;
import com.ecommerce.entity.*;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                      ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public CartDTO getActiveCart(Long userId) {
        Cart cart = cartRepository.findByUserUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> createNewCart(userId));
        return toDTO(cart);
    }

    public CartDTO addItemToCart(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        Cart cart = cartRepository.findByUserUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> createNewCart(userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (product.getStockQuantity() < quantity) {
            throw new BadRequestException("Insufficient stock available");
        }

        // Check if item already exists in cart
        CartItem existingItem = cartItemRepository
                .findByCartCartIdAndProductProductId(cart.getCartId(), productId)
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(product.getUnitPrice())
                    .build();
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        return toDTO(cartRepository.save(cart));
    }

    public CartDTO updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));

        if (!cartItem.getCart().getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Cart item does not belong to user");
        }

        if (cartItem.getProduct().getStockQuantity() < quantity) {
            throw new BadRequestException("Insufficient stock available");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return toDTO(cartItem.getCart());
    }

    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));

        if (!cartItem.getCart().getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Cart item does not belong to user");
        }

        cartItemRepository.delete(cartItem);
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for user"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart createNewCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cart cart = Cart.builder()
                .user(user)
                .status(CartStatus.ACTIVE)
                .build();

        return cartRepository.save(cart);
    }

    private CartDTO toDTO(Cart cart) {
        BigDecimal subtotal = cart.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartDTO.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getUserId())
                .status(cart.getStatus())
                .items(cart.getItems().stream().map(this::toCartItemDTO).collect(Collectors.toList()))
                .subtotal(subtotal)
                .itemCount(cart.getItems().size())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    private CartItemDTO toCartItemDTO(CartItem item) {
        BigDecimal totalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        
        return CartItemDTO.builder()
                .cartItemId(item.getCartItemId())
                .cartId(item.getCart().getCartId())
                .productId(item.getProduct().getProductId())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(totalPrice)
                .build();
    }
}

// Made with Bob
