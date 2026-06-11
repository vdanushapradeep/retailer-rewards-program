package com.example.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO representing the aggregated rewards for a customer, including per-month
 * breakdown and total points.
 */
public class RewardSummaryDto {
    private Long customerId;
    private String customerName;
    private List<MonthlyRewardsDto> monthlyRewards;
    private long totalPoints;
}
