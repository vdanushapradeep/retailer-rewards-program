package com.example.rewards.service;

import com.example.rewards.data.SampleDataLoader;
import com.example.rewards.util.RewardCalculator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

/**
 * Integration-style unit test that verifies the service correctly aggregates
 * the sample transactions loaded at startup.
 */
@SpringBootTest
class RewardsServiceTest {

    @Autowired
    private RewardsService service;

    @Test
    void sampleDataAggregationMatchesCalculator() {
        // Use the sample dataset loaded by SampleDataLoader and ensure that
        // the service aggregates points consistently with RewardCalculator.
        var summaries = service.allCustomerRewards();
        // ensure there is at least one summary
        assertEquals(true, summaries.size() >= 1);

        // verify totals computed by the service match a recompute over the raw transactions
        for (var summary : summaries) {
            long expected = SampleDataLoader.TRANSACTIONS.stream()
                    .filter(t -> t.getCustomerId().equals(summary.getCustomerId()))
                    .mapToLong(t -> RewardCalculator.pointsForAmount(t.getAmount()))
                    .sum();
            assertEquals(expected, summary.getTotalPoints());
        }
    }
}
