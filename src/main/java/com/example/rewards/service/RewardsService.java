package com.example.rewards.service;

import com.example.rewards.data.SampleDataLoader;
import com.example.rewards.model.MonthlyRewards;
import com.example.rewards.model.RewardSummary;
import com.example.rewards.model.Transaction;
import com.example.rewards.util.RewardCalculator;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for calculating reward points aggregated per customer and per month.
 *
 * Responsibilities:
 * - Group transactions by customer id
 * - For each customer, group transactions by year-month (yyyy-MM) derived from the
 *   transaction date and sum the points for that month
 * - Produce a {@link RewardSummary} containing sorted monthly data and a total
 *
 * Implementation notes:
 * - Uses the in-memory {@link SampleDataLoader} for data; replacing with a DAO
 *   or repository would be straightforward.
 * - Month logic is derived programmatically from the transaction date using
 *   a stable ISO-like year-month pattern so month names/ordering aren't hardcoded.
 */
@Service
public class RewardsService {

    private static final DateTimeFormatter YM = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * Calculate rewards for all customers present in the sample transaction list.
     *
     * @return list of {@link RewardSummary} for each customer sorted by customer id
     */
    public List<RewardSummary> allCustomerRewards() {
        Map<String, List<Transaction>> byCustomer = SampleDataLoader.TRANSACTIONS.stream()
                .collect(Collectors.groupingBy(Transaction::getCustomerId));

        return byCustomer.entrySet().stream()
                .map(e -> buildSummary(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(RewardSummary::getCustomerId))
                .collect(Collectors.toList());
    }

    /**
     * Build reward summary for a single customer.
     *
     * Steps:
     * 1) Group transactions by year-month derived from the transaction date
     * 2) Sum points per month using {@link RewardCalculator}
     * 3) Sort and return a {@link RewardSummary}
     *
     * @param customerId id of the customer
     * @param transactions list of the customer's transactions (may be empty)
     * @return constructed {@link RewardSummary}
     */
    private RewardSummary buildSummary(String customerId, List<Transaction> transactions) {
        Map<String, Long> perMonth = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getDate().format(YM),
                        Collectors.summingLong(t -> RewardCalculator.pointsForAmount(t.getAmount()))));

        List<MonthlyRewards> monthlyRewards = perMonth.entrySet().stream()
                .map(e -> new MonthlyRewards(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(MonthlyRewards::getYearMonth))
                .collect(Collectors.toList());

        long total = monthlyRewards.stream().mapToLong(MonthlyRewards::getPoints).sum();

        String name = SampleDataLoader.CUSTOMERS.getOrDefault(customerId, null) != null
                ? SampleDataLoader.CUSTOMERS.get(customerId).getName() : "";

        return new RewardSummary(customerId, name, monthlyRewards, total);
    }
}
