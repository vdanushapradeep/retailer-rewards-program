package com.example.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO representing rewards points for a single year-month period.
 */
public class MonthlyRewardsDto {
    private String yearMonth;
    private BigDecimal points;
}
