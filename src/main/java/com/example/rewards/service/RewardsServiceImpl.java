package com.example.rewards.service;

import com.example.rewards.dto.MonthlyRewardsDto;
import com.example.rewards.dto.RewardSummaryDto;
import com.example.rewards.entity.TransactionEntity;
import com.example.rewards.exception.ResourceNotFoundException;
import com.example.rewards.repository.CustomerRepository;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.util.RewardCalculator;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
/**
 * Implementation of {@link RewardsService} that queries transactions and
 * customers from repositories and aggregates reward points per month.
 */
public class RewardsServiceImpl implements RewardsService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final RewardCalculator calculator;

    private static final DateTimeFormatter YM = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * Construct the service with required repositories and calculator.
     *
     * @param transactionRepository repository to read transactions from
     * @param customerRepository    repository to read customers from
     * @param calculator            calculator used to compute reward points
     */
    public RewardsServiceImpl(TransactionRepository transactionRepository,
            CustomerRepository customerRepository,
            RewardCalculator calculator) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.calculator = calculator;
    }

    @Override
    /**
     * Aggregate reward summaries for every customer by grouping transactions
     * by customer id and then building {@link RewardSummaryDto} objects.
     *
     * @return list of reward summaries for all customers
     */
    public List<RewardSummaryDto> getAllCustomerRewards() {
        // Business rule: only consider transactions from the last 90 days
        LocalDate cutoff = LocalDate.now().minusDays(90);

        List<TransactionEntity> all = transactionRepository.findAll().stream()
                .filter(t -> !t.getTransactionDate().isBefore(cutoff))
                .collect(Collectors.toList());

        Map<Long, List<TransactionEntity>> byCustomer = all.stream()
                .collect(Collectors.groupingBy(t -> t.getCustomer().getId()));

        return byCustomer.entrySet().stream()
                .map(e -> buildSummaryDto(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(RewardSummaryDto::getCustomerId))
                .collect(Collectors.toList());
    }

    @Override
    /**
     * Return reward summary for a single customer.
     *
     * @param customerId id of the customer; must not be null
     * @return the reward summary for the customer
     * @throws ResourceNotFoundException when the customer does not exist
     */
    public RewardSummaryDto getCustomerRewards(Long customerId) {
        Long id = Objects.requireNonNull(customerId, "customerId");
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));

        // Business rule: only consider transactions from the last 90 days
        LocalDate cutoff = LocalDate.now().minusDays(90);

        List<TransactionEntity> transactions = transactionRepository.findAll().stream()
                .filter(t -> t.getCustomer().getId().equals(id))
                .filter(t -> !t.getTransactionDate().isBefore(cutoff))
                .collect(Collectors.toList());
        return buildSummaryDto(id, transactions, customer.getName());
    }

    /**
     * Helper that builds a {@link RewardSummaryDto} when the customer name is
     * not provided; the name will be resolved from the {@code customerRepository}.
     *
     * @param customerId   id of the customer
     * @param transactions transactions belonging to the customer
     * @return assembled reward summary DTO
     */
    private RewardSummaryDto buildSummaryDto(Long customerId,
            List<TransactionEntity> transactions) {
        String name = customerRepository.findById(customerId).map(c -> c.getName()).orElse("");
        return buildSummaryDto(customerId, transactions, name);
    }

    /**
     * Build the reward summary DTO using pre-resolved customer name.
     *
     * @param customerId   id of the customer
     * @param transactions list of transactions for the customer
     * @param name         customer name (may be empty)
     * @return aggregated reward summary DTO
     */
    private RewardSummaryDto buildSummaryDto(Long customerId,
            List<TransactionEntity> transactions, String name) {
        Map<String, Long> perMonth = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getTransactionDate().format(YM),
                        Collectors.summingLong(t -> calculator.pointsForAmount(t.getAmount()))));

        List<MonthlyRewardsDto> monthlyRewards = perMonth.entrySet().stream()
                .map(e -> new MonthlyRewardsDto(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(MonthlyRewardsDto::getYearMonth))
                .collect(Collectors.toList());

        long total = monthlyRewards.stream().mapToLong(MonthlyRewardsDto::getPoints).sum();

        return new RewardSummaryDto(customerId, name, monthlyRewards, total);
    }
}
