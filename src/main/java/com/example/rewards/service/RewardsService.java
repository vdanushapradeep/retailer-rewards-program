package com.example.rewards.service;

import com.example.rewards.dto.RewardSummaryDto;

import java.util.List;

/**
 * Service interface defining operations to retrieve reward summaries.
 */
public interface RewardsService {

        /**
         * Aggregate reward summaries for customers in a paged view.
         *
         * @param page zero-based page index
         * @param size page size
         * @return current page content of reward summaries
         */
        List<RewardSummaryDto> getAllCustomerRewards(int page, int size);

        /**
         * Retrieve reward summary for a single customer.
         *
         * @param customerId id of the customer
         * @return reward summary DTO
         */
        RewardSummaryDto getCustomerRewards(Long customerId);

}
