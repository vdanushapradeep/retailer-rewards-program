package com.example.rewards.repository;

import com.example.rewards.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for {@link TransactionEntity} persistence operations.
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    /**
     * Fetch all transactions on or after the given date (used for all-customers report).
     */
    List<TransactionEntity> findByTransactionDateGreaterThanEqual(LocalDate fromDate);

    /**
     * Fetch transactions for a specific customer on or after the given date.
     */
    List<TransactionEntity> findByCustomerIdAndTransactionDateGreaterThanEqual(Long customerId, LocalDate fromDate);
}
