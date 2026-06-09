package com.example.rewards.mapper;

import com.example.rewards.dto.MonthlyRewardsDto;
import com.example.rewards.dto.RewardSummaryDto;
import com.example.rewards.entity.TransactionEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
/**
 * Mapper that converts entities and raw aggregation maps into DTOs used by
 * the REST layer.
 */
public class EntityToDtoMapper {

        /**
         * Convert a per-month points map into a {@link RewardSummaryDto}.
         *
         * @param customerId id of the customer
         * @param name       customer name
         * @param perMonth   map from year-month to points
         * @return constructed {@link RewardSummaryDto}
         */
        public RewardSummaryDto toRewardSummary(Long customerId, String name, Map<String, Long> perMonth) {
                List<MonthlyRewardsDto> monthly = perMonth.entrySet().stream()
                                .map(e -> new MonthlyRewardsDto(e.getKey(), e.getValue()))
                                .sorted((a, b) -> a.getYearMonth().compareTo(b.getYearMonth()))
                                .collect(Collectors.toList());

                long total = monthly.stream().mapToLong(MonthlyRewardsDto::getPoints).sum();

                return RewardSummaryDto.builder()
                                .customerId(customerId)
                                .customerName(name)
                                .monthlyRewards(monthly)
                                .totalPoints(total)
                                .build();
        }

        /**
         * Aggregate transactions into a map keyed by year-month (yyyy-MM) and
         * with summed points.
         *
         * @param transactions list of transactions to aggregate
         * @return map of year-month strings to summed points
         */
        public Map<String, Long> aggregateByMonth(List<TransactionEntity> transactions) {
                return transactions.stream()
                                .collect(Collectors.groupingBy(
                                                t -> t.getTransactionDate()
                                                                .format(java.time.format.DateTimeFormatter
                                                                                .ofPattern("yyyy-MM")),
                                                Collectors.summingLong(t -> 0L)));
        }
}
