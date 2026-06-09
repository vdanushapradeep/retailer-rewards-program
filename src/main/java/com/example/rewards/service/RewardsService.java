package com.example.rewards.service;

import com.example.rewards.dto.RewardSummaryDto;

import java.util.List;

/**
 * Service interface defining operations to retrieve reward summaries.
 */
public interface RewardsService {

        /**
         * Aggregate reward summaries for every customer.
         *
         * @return list of reward summaries
         */
        List<RewardSummaryDto> getAllCustomerRewards();

        /**
         * Retrieve reward summary for a single customer.
         *
         * @param customerId id of the customer
         * @return reward summary DTO
         */
        RewardSummaryDto getCustomerRewards(Long customerId);

}
