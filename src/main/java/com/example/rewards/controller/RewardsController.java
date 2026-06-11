package com.example.rewards.controller;

import com.example.rewards.dto.RewardSummaryDto;
import com.example.rewards.service.RewardsService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing rewards endpoints.
 *
 * Endpoints:
 * - GET /api/rewards -> list all customers' reward summaries
 * - GET /api/rewards/{customerId} -> single customer's reward summary
 */
@RestController
@RequestMapping("/api/rewards")
@Validated
public class RewardsController {

    private final RewardsService rewardsService;

    /**
     * Create controller with the provided {@link RewardsService}.
     *
     * @param rewardsService service used to fetch reward summaries
     */
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    /**
     * Return all customers' reward summaries.
     *
     * @return 200 OK with only the current page content
     */
    @GetMapping
    public ResponseEntity<List<RewardSummaryDto>> all(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return ResponseEntity.ok(rewardsService.getAllCustomerRewards(page, size));
    }

    /**
     * Return a single customer's reward summary.
     *
     * @param customerId identifier of the customer
     * @return 200 OK with the summary when found, or 404 Not Found when no such
     *         customer exists
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<RewardSummaryDto> byCustomer(@PathVariable @Min(1) Long customerId) {
        return ResponseEntity.ok(rewardsService.getCustomerRewards(customerId));
    }
}
