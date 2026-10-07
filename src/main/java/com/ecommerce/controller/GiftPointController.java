package com.ecommerce.controller;

import com.ecommerce.dto.CreditGiftPointsRequest;
import com.ecommerce.dto.GiftPointAccountDTO;
import com.ecommerce.dto.GiftPointTransactionDTO;
import com.ecommerce.dto.RedeemGiftPointsRequest;
import com.ecommerce.service.GiftPointService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gift-points")
@Tag(name = "Gift Points", description = "Gift points account and transactions")
@SecurityRequirement(name = "bearerAuth")
public class GiftPointController {

    private final GiftPointService giftPointService;

    public GiftPointController(GiftPointService giftPointService) {
        this.giftPointService = giftPointService;
    }

    @GetMapping("/account")
    public ResponseEntity<GiftPointAccountDTO> getAccount(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(giftPointService.getOrCreateAccount(userId));
    }

    @GetMapping("/balance")
    public ResponseEntity<GiftPointAccountDTO> getBalance(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(giftPointService.getAccountBalance(userId));
    }

    @PostMapping("/credit")
    public ResponseEntity<GiftPointTransactionDTO> creditPoints(@Valid @RequestBody CreditGiftPointsRequest request,
                                                               Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(giftPointService.creditPoints(
                userId,
                request.getAmount(),
                request.getVoucherCode(),
                request.getDescription()
        ));
    }

    @PostMapping("/redeem")
    public ResponseEntity<GiftPointTransactionDTO> redeemPoints(@Valid @RequestBody RedeemGiftPointsRequest request,
                                                               Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(giftPointService.redeemPoints(
                userId,
                request.getAmount(),
                request.getDescription()
        ));
    }

    @GetMapping("/transactions")
    public ResponseEntity<Page<GiftPointTransactionDTO>> getTransactionHistory(
            Authentication authentication,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(giftPointService.getTransactionHistory(userId, pageable));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<GiftPointTransactionDTO> getTransactionById(@PathVariable Long id,
                                                                     Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(giftPointService.getTransactionById(userId, id));
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }
}

// Made with Bob
