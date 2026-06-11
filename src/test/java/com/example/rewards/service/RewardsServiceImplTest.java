package com.example.rewards.service;

import com.example.rewards.entity.CustomerEntity;
import com.example.rewards.entity.TransactionEntity;
import com.example.rewards.exception.ResourceNotFoundException;
import com.example.rewards.repository.CustomerRepository;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.util.RewardCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
     * Build a few transactions and verify monthly aggregation and totals are
     * computed.
     */
    @Test
    void buildSummaryForCustomerAggregatesMonthsAndTotals() {
        TransactionRepository txRepo = mock(TransactionRepository.class);
        CustomerRepository custRepo = mock(CustomerRepository.class);
        RewardCalculator calc = new RewardCalculator();

        CustomerEntity c = CustomerEntity.builder().id(1L).name("Bob").build();
        // use dates within the last 90 days so business rule includes them
        TransactionEntity t1 = TransactionEntity.builder().customer(c).amount(BigDecimal.valueOf(120))
                .transactionDate(LocalDate.now().minusDays(30)).build();
        TransactionEntity t2 = TransactionEntity.builder().customer(c).amount(BigDecimal.valueOf(75))
                .transactionDate(LocalDate.now().minusDays(60)).build();

        when(custRepo.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(c), PageRequest.of(0, 10), 1));
        when(txRepo.findByCustomerIdInAndTransactionDateGreaterThanEqual(anyList(), any(LocalDate.class)))
                .thenReturn(List.of(t1, t2));
        when(txRepo.findByCustomerIdAndTransactionDateGreaterThanEqual(any(Long.class), any(LocalDate.class)))
                .thenReturn(List.of(t1, t2));
        when(custRepo.findById(1L)).thenReturn(Optional.of(c));

        RewardsServiceImpl svc = new RewardsServiceImpl(txRepo, custRepo, calc);

        var all = svc.getAllCustomerRewards(0, 10);
        assertEquals(1, all.size());

        var summary = svc.getCustomerRewards(1L);
        assertEquals(1L, summary.getCustomerId());
        assertEquals("Bob", summary.getCustomerName());
        assertEquals(2, summary.getMonthlyRewards().size());
        assertEquals(115L, summary.getTotalPoints());
    }

    @Test
    void getAllCustomerRewardsReturnsEmptyWhenPageHasNoCustomers() {
        TransactionRepository txRepo = mock(TransactionRepository.class);
        CustomerRepository custRepo = mock(CustomerRepository.class);
        RewardCalculator calc = new RewardCalculator();

        when(custRepo.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(99, 10), 10));

        RewardsServiceImpl svc = new RewardsServiceImpl(txRepo, custRepo, calc);

        var all = svc.getAllCustomerRewards(99, 10);
        assertTrue(all.isEmpty());
        verify(txRepo, never()).findByCustomerIdInAndTransactionDateGreaterThanEqual(anyList(), any(LocalDate.class));
    }

    @Test
    void getAllCustomerRewardsIncludesCustomerWithZeroTransactions() {
        TransactionRepository txRepo = mock(TransactionRepository.class);
        CustomerRepository custRepo = mock(CustomerRepository.class);
        RewardCalculator calc = new RewardCalculator();

        CustomerEntity c = CustomerEntity.builder().id(7L).name("NoTx").build();
        when(custRepo.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(c), PageRequest.of(0, 10), 1));
        when(txRepo.findByCustomerIdInAndTransactionDateGreaterThanEqual(anyList(), any(LocalDate.class)))
                .thenReturn(List.of());

        RewardsServiceImpl svc = new RewardsServiceImpl(txRepo, custRepo, calc);

        var all = svc.getAllCustomerRewards(0, 10);
        assertEquals(1, all.size());
        assertEquals(7L, all.get(0).getCustomerId());
        assertEquals("NoTx", all.get(0).getCustomerName());
        assertEquals(0L, all.get(0).getTotalPoints());
        assertTrue(all.get(0).getMonthlyRewards().isEmpty());
    }
}
