package com.example.rewards.service;

import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.util.RewardCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration-style unit test that verifies the service correctly aggregates
 * the sample transactions loaded at startup.
 */
@SpringBootTest
class RewardsServiceTest {

    @Autowired
    private RewardsService service;

    @Autowired
    private TransactionRepository transactionRepository;

    @Value("${rewards.transaction-lookback-days:90}")
    private long transactionLookbackDays;

    @Test
    void sampleDataAggregationMatchesCalculator() {
        // Use the sample dataset loaded by SampleDataLoader and ensure that
        // the service aggregates points consistently with RewardCalculator.
        var summaries = service.getAllCustomerRewards(0, 100);
        // ensure there is at least one summary
        assertEquals(true, summaries.size() >= 1);

        // verify totals computed by the service match a recompute over the raw
        // transactions
        LocalDate cutoff = LocalDate.now().minusDays(transactionLookbackDays);
        for (var summary : summaries) {
            BigDecimal expected = transactionRepository
                    .findByCustomerIdAndTransactionDateGreaterThanEqual(summary.getCustomerId(), cutoff)
                    .stream()
                    .map(t -> new RewardCalculator().pointsForAmount(t.getAmount()))
                    .reduce(new BigDecimal("0.00"), BigDecimal::add);
            assertEquals(expected, summary.getTotalPoints());
        }
    }
}
