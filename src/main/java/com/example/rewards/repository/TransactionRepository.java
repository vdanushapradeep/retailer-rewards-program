package com.example.rewards.repository;

import com.example.rewards.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link TransactionEntity} persistence operations.
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
