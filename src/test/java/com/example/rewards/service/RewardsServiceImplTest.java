package com.example.rewards.service;

import com.example.rewards.entity.CustomerEntity;
import com.example.rewards.entity.TransactionEntity;
import com.example.rewards.exception.ResourceNotFoundException;
import com.example.rewards.repository.CustomerRepository;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.util.RewardCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link RewardsServiceImpl} covering both error conditions
 * and aggregation behavior. Consolidated so there is one test class per
 * production class.
 */
class RewardsServiceImplTest {

    /**
     * Ensure a missing customer id causes a {@link ResourceNotFoundException}.
     */
    @Test
    void getCustomerRewardsThrowsWhenCustomerMissing() {
        TransactionRepository txRepo = mock(TransactionRepository.class);
        CustomerRepository custRepo = mock(CustomerRepository.class);
        RewardCalculator calc = new RewardCalculator();

        when(custRepo.findById(42L)).thenReturn(Optional.empty());

        RewardsServiceImpl svc = new RewardsServiceImpl(txRepo, custRepo, calc);

        assertThrows(ResourceNotFoundException.class, () -> svc.getCustomerRewards(42L));
    }

    /**
     * Build a few transactions and verify monthly aggregation and totals are computed.
     */
    @Test
    void buildSummaryForCustomerAggregatesMonthsAndTotals() {
        TransactionRepository txRepo = mock(TransactionRepository.class);
        CustomerRepository custRepo = mock(CustomerRepository.class);
        RewardCalculator calc = new RewardCalculator();

        CustomerEntity c = CustomerEntity.builder().id(1L).name("Bob").build();
        // use dates within the last 90 days so business rule includes them
        TransactionEntity t1 = TransactionEntity.builder().customer(c).amount(BigDecimal.valueOf(120)).transactionDate(LocalDate.now().minusDays(30)).build();
        TransactionEntity t2 = TransactionEntity.builder().customer(c).amount(BigDecimal.valueOf(75)).transactionDate(LocalDate.now().minusDays(60)).build();

        when(txRepo.findByTransactionDateGreaterThanEqual(any(LocalDate.class))).thenReturn(List.of(t1, t2));
        when(txRepo.findByCustomerIdAndTransactionDateGreaterThanEqual(any(Long.class), any(LocalDate.class))).thenReturn(List.of(t1, t2));
        when(custRepo.findById(1L)).thenReturn(Optional.of(c));

        RewardsServiceImpl svc = new RewardsServiceImpl(txRepo, custRepo, calc);

        var all = svc.getAllCustomerRewards();
        assertEquals(1, all.size());

        var summary = svc.getCustomerRewards(1L);
        assertEquals(1L, summary.getCustomerId());
        assertEquals("Bob", summary.getCustomerName());
        assertEquals(2, summary.getMonthlyRewards().size());
        assertEquals(115L, summary.getTotalPoints());
    }
}
