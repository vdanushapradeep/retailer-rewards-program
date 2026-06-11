package com.example.rewards.repository;

import com.example.rewards.entity.TransactionEntity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link TransactionEntity} persistence operations.
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    /**
     * Fetch transactions for a specific customer on or after the given date.
     */
    List<TransactionEntity> findByCustomerIdAndTransactionDateGreaterThanEqual(Long customerId, LocalDate fromDate);

    /**
     * Fetch transactions for a set of customers on or after the given date.
     */
    List<TransactionEntity> findByCustomerIdInAndTransactionDateGreaterThanEqual(List<Long> customerIds,
            LocalDate fromDate);
}
