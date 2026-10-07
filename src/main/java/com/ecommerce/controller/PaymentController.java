package com.ecommerce.controller;

import com.ecommerce.dto.InitiatePaymentRequest;
import com.ecommerce.dto.PaymentDTO;
import com.ecommerce.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Payment processing operations")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<PaymentDTO> initiatePayment(@Valid @RequestBody InitiatePaymentRequest request,
                                                     Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        PaymentDTO payment = paymentService.initiatePayment(
                userId,
                request.getOrderId(),
                request.getPaymentMethod()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<PaymentDTO> completePayment(@PathVariable Long id,
                                                     Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(paymentService.completePayment(userId, id));
    }

    @PostMapping("/{id}/fail")
    public ResponseEntity<PaymentDTO> failPayment(@PathVariable Long id,
                                                 Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(paymentService.failPayment(userId, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(paymentService.getPaymentById(userId, id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentDTO> getPaymentByOrderId(@PathVariable Long orderId,
                                                         Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(userId, orderId));
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getUserPayments(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(paymentService.getUserPayments(userId));
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }
}

// Made with Bob
