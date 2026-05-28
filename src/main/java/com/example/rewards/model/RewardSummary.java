package com.example.rewards.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO containing a customer's reward breakdown per month and total points.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RewardSummary {
    private String customerId;
    private String customerName;
    private List<MonthlyRewards> monthlyRewards;
    private long totalPoints;
}
