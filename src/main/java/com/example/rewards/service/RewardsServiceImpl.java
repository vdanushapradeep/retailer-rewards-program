package com.example.rewards.service;

import com.example.rewards.dto.MonthlyRewardsDto;
import com.example.rewards.dto.RewardSummaryDto;
import com.example.rewards.entity.TransactionEntity;
import com.example.rewards.exception.ResourceNotFoundException;
import com.example.rewards.repository.CustomerRepository;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.util.RewardCalculator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        public List<RewardSummaryDto> getAllCustomerRewards(int page, int size) {
                Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
                var customerPage = customerRepository.findAll(pageable);
                List<Long> customerIds = customerPage.getContent().stream()
                                .map(c -> c.getId())
                                .collect(Collectors.toList());

                LocalDate cutoff = LocalDate.now().minusDays(90);
                List<TransactionEntity> transactions = customerIds.isEmpty()
                                ? List.of()
                                : transactionRepository.findByCustomerIdInAndTransactionDateGreaterThanEqual(
                                                customerIds, cutoff);

                Map<Long, List<TransactionEntity>> byCustomer = transactions.stream()
                                .collect(Collectors.groupingBy(t -> t.getCustomer().getId()));

                List<RewardSummaryDto> content = customerPage.getContent().stream()
                                .map(customer -> buildSummaryDto(
                                                customer.getId(),
                                                byCustomer.getOrDefault(customer.getId(), List.of()),
                                                customer.getName()))
                                .collect(Collectors.toList());

                return content;
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

                List<TransactionEntity> transactions = transactionRepository
                                .findByCustomerIdAndTransactionDateGreaterThanEqual(id, cutoff);
                return buildSummaryDto(id, transactions, customer.getName());
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
                                                Collectors.summingLong(
                                                                t -> calculator.pointsForAmount(t.getAmount()))));

                List<MonthlyRewardsDto> monthlyRewards = perMonth.entrySet().stream()
                                .map(e -> new MonthlyRewardsDto(e.getKey(), e.getValue()))
                                .sorted(Comparator.comparing(MonthlyRewardsDto::getYearMonth))
                                .collect(Collectors.toList());

                long total = monthlyRewards.stream().mapToLong(MonthlyRewardsDto::getPoints).sum();

                return new RewardSummaryDto(customerId, name, monthlyRewards, total);
        }
}
