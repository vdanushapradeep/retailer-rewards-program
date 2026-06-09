package com.example.rewards.repository;

import com.example.rewards.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link CustomerEntity} persistence operations.
 */
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}
