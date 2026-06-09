package com.example.rewards.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO representing rewards points for a single year-month period.
 */
public class MonthlyRewardsDto {
    private String yearMonth;
    private long points;
}
