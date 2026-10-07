package com.ecommerce.repository;

import com.ecommerce.entity.GiftPointTransaction;
import com.ecommerce.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftPointTransactionRepository extends JpaRepository<GiftPointTransaction, Long> {
    
    Page<GiftPointTransaction> findByGiftPointAccountGiftPointAccountId(Long accountId, Pageable pageable);
    
    Page<GiftPointTransaction> findByGiftPointAccountGiftPointAccountIdAndTransactionType(
        Long accountId, 
        TransactionType transactionType, 
        Pageable pageable
    );
}

// Made with Bob
