package com.ecommerce.repository;

import com.ecommerce.entity.GiftPointAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GiftPointAccountRepository extends JpaRepository<GiftPointAccount, Long> {
    
    Optional<GiftPointAccount> findByUserUserId(Long userId);
}

// Made with Bob
