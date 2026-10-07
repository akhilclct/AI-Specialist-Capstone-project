package com.ecommerce.service;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.OrderItemDTO;
import com.ecommerce.dto.AddressDTO;
import com.ecommerce.entity.*;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Value("${app.default-currency}")
    private String defaultCurrency;

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final GiftPointService giftPointService;
    private final PaymentRepository paymentRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository,
                       AddressRepository addressRepository, UserRepository userRepository,
                       ProductRepository productRepository, GiftPointService giftPointService,
                       PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.giftPointService = giftPointService;
        this.paymentRepository = paymentRepository;
    }

    public Page<OrderDTO> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserUserId(userId, pageable)
                .map(this::toDTO);
    }

    public OrderDTO getOrderById(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Order does not belong to user");
        }

        return toDTO(order);
    }

    public OrderDTO createOrder(Long userId, Long deliveryAddressId, BigDecimal giftPointsToRedeem) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Address deliveryAddress = addressRepository.findById(deliveryAddressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", deliveryAddressId));

        if (!deliveryAddress.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Address does not belong to user");
        }

        Cart cart = cartRepository.findByUserUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new BadRequestException("No active cart found"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        // Calculate subtotal
        BigDecimal subtotal = cart.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Apply gift points
        BigDecimal giftPointsRedeemed = BigDecimal.ZERO;
        if (giftPointsToRedeem != null && giftPointsToRedeem.compareTo(BigDecimal.ZERO) > 0) {
            giftPointService.redeemPoints(userId, giftPointsToRedeem, "Applied to order");
            giftPointsRedeemed = giftPointsToRedeem;
        }
        BigDecimal totalAmount = subtotal.subtract(giftPointsRedeemed);

        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmount = BigDecimal.ZERO;
        }

        boolean fullyPaidByGiftPoints = totalAmount.compareTo(BigDecimal.ZERO) == 0;

        // Create order
        Order order = Order.builder()
                .user(user)
                .deliveryAddress(deliveryAddress)
                .orderStatus(fullyPaidByGiftPoints ? OrderStatus.CONFIRMED : OrderStatus.PENDING)
                .subtotal(subtotal)
                .giftPointsRedeemed(giftPointsRedeemed)
                .totalAmount(totalAmount)
                .currencyCode(defaultCurrency)
                .orderedAt(LocalDateTime.now())
                .build();

        // Create order items from cart items
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .productId(cartItem.getProduct().getProductId())
                        .productName(cartItem.getProduct().getName())
                        .unitPrice(cartItem.getUnitPrice())
                        .quantity(cartItem.getQuantity())
                        .totalPrice(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);

        // Mark cart as checked out
        cart.setStatus(CartStatus.CHECKED_OUT);
        cartRepository.save(cart);

        Order savedOrder = orderRepository.save(order);

        // If fully covered by gift points, record a completed zero-amount payment — no gateway needed
        if (fullyPaidByGiftPoints) {
            Payment giftPayment = Payment.builder()
                    .order(savedOrder)
                    .paymentMethod(PaymentMethod.GIFT_VOUCHER)
                    .amount(BigDecimal.ZERO)
                    .currencyCode(defaultCurrency)
                    .paymentStatus(PaymentStatus.COMPLETED)
                    .transactionReference("GIFT-" + savedOrder.getOrderId())
                    .initiatedAt(LocalDateTime.now())
                    .completedAt(LocalDateTime.now())
                    .build();
            paymentRepository.save(giftPayment);
        }

        return toDTO(savedOrder);
    }

    public void buyAgain(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Order does not belong to user");
        }

        Cart cart = cartRepository.findByUserUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
                    return cartRepository.save(Cart.builder()
                            .user(user)
                            .status(CartStatus.ACTIVE)
                            .build());
                });

        // Add order items to cart
        for (OrderItem orderItem : order.getItems()) {
            Product product = productRepository.findById(orderItem.getProductId())
                    .orElse(null);

            if (product != null && product.getStockQuantity() >= orderItem.getQuantity()) {
                CartItem cartItem = CartItem.builder()
                        .cart(cart)
                        .product(product)
                        .quantity(orderItem.getQuantity())
                        .unitPrice(product.getUnitPrice())
                        .build();
                cart.getItems().add(cartItem);
            }
        }

        cartRepository.save(cart);
    }

    private OrderDTO toDTO(Order order) {
        AddressDTO addressDTO = AddressDTO.builder()
                .addressId(order.getDeliveryAddress().getAddressId())
                .addressLine1(order.getDeliveryAddress().getAddressLine1())
                .addressLine2(order.getDeliveryAddress().getAddressLine2())
                .city(order.getDeliveryAddress().getCity())
                .state(order.getDeliveryAddress().getState())
                .postalCode(order.getDeliveryAddress().getPostalCode())
                .countryCode(order.getDeliveryAddress().getCountryCode())
                .build();

        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .orderItemId(item.getOrderItemId())
                        .orderId(item.getOrder().getOrderId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .deliveryAddressId(order.getDeliveryAddress().getAddressId())
                .deliveryAddress(addressDTO)
                .orderStatus(order.getOrderStatus())
                .subtotal(order.getSubtotal())
                .giftPointsRedeemed(order.getGiftPointsRedeemed())
                .totalAmount(order.getTotalAmount())
                .currencyCode(order.getCurrencyCode())
                .orderedAt(order.getOrderedAt())
                .items(itemDTOs)
                .build();
    }
}

// Made with Bob
