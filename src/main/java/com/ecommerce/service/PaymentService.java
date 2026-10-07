package com.ecommerce.service;

import com.ecommerce.dto.PaymentDTO;
import com.ecommerce.entity.*;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public PaymentDTO initiatePayment(Long userId, Long orderId, PaymentMethod paymentMethod) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Order does not belong to user");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Order is not in pending status");
        }

        // Check if payment already exists for this order
        if (paymentRepository.existsByOrderOrderId(orderId)) {
            throw new BadRequestException("Payment already initiated for this order");
        }

        Payment payment = Payment.builder()
                .order(order)
                .paymentMethod(paymentMethod)
                .amount(order.getTotalAmount())
                .currencyCode(order.getCurrencyCode())
                .paymentStatus(PaymentStatus.INITIATED)
                .transactionReference(generateTransactionId())
                .initiatedAt(LocalDateTime.now())
                .build();

        return toDTO(paymentRepository.save(payment));
    }

    public PaymentDTO completePayment(Long userId, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        if (!payment.getOrder().getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Payment does not belong to user");
        }

        if (payment.getPaymentStatus() != PaymentStatus.INITIATED) {
            throw new BadRequestException("Payment is not in initiated status");
        }

        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setCompletedAt(LocalDateTime.now());

        // Update order status
        Order order = payment.getOrder();
        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        return toDTO(paymentRepository.save(payment));
    }

    public PaymentDTO failPayment(Long userId, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        if (!payment.getOrder().getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Payment does not belong to user");
        }

        if (payment.getPaymentStatus() != PaymentStatus.INITIATED) {
            throw new BadRequestException("Payment is not in initiated status");
        }

        payment.setPaymentStatus(PaymentStatus.FAILED);
        payment.setCompletedAt(LocalDateTime.now());

        return toDTO(paymentRepository.save(payment));
    }

    public PaymentDTO getPaymentById(Long userId, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        if (!payment.getOrder().getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Payment does not belong to user");
        }

        return toDTO(payment);
    }

    public PaymentDTO getPaymentByOrderId(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Order does not belong to user");
        }

        Payment payment = paymentRepository.findByOrderOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "orderId", orderId));

        return toDTO(payment);
    }

    public List<PaymentDTO> getUserPayments(Long userId) {
        return paymentRepository.findByOrderUserUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private PaymentDTO toDTO(Payment payment) {
        return PaymentDTO.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrder().getOrderId())
                .paymentMethod(payment.getPaymentMethod())
                .amount(payment.getAmount())
                .currencyCode(payment.getCurrencyCode())
                .paymentStatus(payment.getPaymentStatus())
                .transactionReference(payment.getTransactionReference())
                .initiatedAt(payment.getInitiatedAt())
                .completedAt(payment.getCompletedAt())
                .build();
    }
}

// Made with Bob
