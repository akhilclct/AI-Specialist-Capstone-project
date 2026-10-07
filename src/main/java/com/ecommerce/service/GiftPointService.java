package com.ecommerce.service;

import com.ecommerce.dto.GiftPointAccountDTO;
import com.ecommerce.dto.GiftPointTransactionDTO;
import com.ecommerce.entity.*;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.GiftPointAccountRepository;
import com.ecommerce.repository.GiftPointTransactionRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class GiftPointService {

    private final GiftPointAccountRepository accountRepository;
    private final GiftPointTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public GiftPointService(GiftPointAccountRepository accountRepository,
                            GiftPointTransactionRepository transactionRepository,
                            UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public GiftPointAccountDTO getOrCreateAccount(Long userId) {
        GiftPointAccount account = accountRepository.findByUserUserId(userId)
                .orElseGet(() -> createAccount(userId));
        return toAccountDTO(account);
    }

    @Transactional(readOnly = true)
    public GiftPointAccountDTO getAccountBalance(Long userId) {
        GiftPointAccount account = accountRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("GiftPointAccount", "userId", userId));
        return toAccountDTO(account);
    }

    public GiftPointTransactionDTO redeemPoints(Long userId, BigDecimal amount, String description) {
        GiftPointAccount account = accountRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("GiftPointAccount", "userId", userId));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Insufficient gift point balance");
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        GiftPointTransaction transaction = GiftPointTransaction.builder()
                .giftPointAccount(account)
                .transactionType(TransactionType.REDEEM)
                .points(amount)
                .description(description)
                .build();

        GiftPointTransaction saved = transactionRepository.save(transaction);
        return toTransactionDTO(saved, newBalance);
    }

    public GiftPointTransactionDTO creditPoints(Long userId, BigDecimal amount, String voucherCode, String description) {
        GiftPointAccount account = accountRepository.findByUserUserId(userId)
                .orElseGet(() -> createAccount(userId));

        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        String txDescription = (description != null && !description.isBlank())
                ? description
                : "Gift voucher redeemed: " + voucherCode;

        GiftPointTransaction transaction = GiftPointTransaction.builder()
                .giftPointAccount(account)
                .transactionType(TransactionType.ADJUST)
                .points(amount)
                .description(txDescription)
                .build();

        GiftPointTransaction saved = transactionRepository.save(transaction);
        return toTransactionDTO(saved, newBalance);
    }

    public GiftPointTransactionDTO earnPoints(Long userId, BigDecimal amount, Long orderId) {
        GiftPointAccount account = accountRepository.findByUserUserId(userId)
                .orElseGet(() -> createAccount(userId));

        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        GiftPointTransaction transaction = GiftPointTransaction.builder()
                .giftPointAccount(account)
                .orderId(orderId)
                .transactionType(TransactionType.EARN)
                .points(amount)
                .description("Points earned for order #" + orderId)
                .build();

        GiftPointTransaction saved = transactionRepository.save(transaction);
        return toTransactionDTO(saved, newBalance);
    }

    @Transactional(readOnly = true)
    public Page<GiftPointTransactionDTO> getTransactionHistory(Long userId, Pageable pageable) {
        GiftPointAccount account = accountRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("GiftPointAccount", "userId", userId));

        return transactionRepository
                .findByGiftPointAccountGiftPointAccountId(account.getGiftPointAccountId(), pageable)
                .map(tx -> toTransactionDTO(tx, null));
    }

    @Transactional(readOnly = true)
    public GiftPointTransactionDTO getTransactionById(Long userId, Long transactionId) {
        GiftPointTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("GiftPointTransaction", "id", transactionId));

        Long ownerUserId = transaction.getGiftPointAccount().getUser().getUserId();
        if (!ownerUserId.equals(userId)) {
            throw new BadRequestException("Transaction does not belong to user");
        }

        return toTransactionDTO(transaction, null);
    }

    // --- helpers ---

    private GiftPointAccount createAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return accountRepository.save(GiftPointAccount.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .build());
    }

    private GiftPointAccountDTO toAccountDTO(GiftPointAccount account) {
        return GiftPointAccountDTO.builder()
                .giftPointAccountId(account.getGiftPointAccountId())
                .userId(account.getUser().getUserId())
                .balance(account.getBalance())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    private GiftPointTransactionDTO toTransactionDTO(GiftPointTransaction tx, BigDecimal balanceAfter) {
        return GiftPointTransactionDTO.builder()
                .transactionId(tx.getGiftPointTransactionId())
                .accountId(tx.getGiftPointAccount().getGiftPointAccountId())
                .transactionType(tx.getTransactionType())
                .amount(tx.getPoints())
                .balanceAfter(balanceAfter)
                .description(tx.getDescription())
                .transactionDate(tx.getCreatedAt())
                .build();
    }
}

// Made with Bob
