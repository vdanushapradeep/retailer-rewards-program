package com.example.rewards.controller;

import com.example.rewards.model.RewardSummary;
import com.example.rewards.service.RewardsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing rewards endpoints.
 *
 * Endpoints:
 * - GET /api/rewards -> list all customers' reward summaries
 * - GET /api/rewards/{customerId} -> single customer's reward summary
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    private final RewardsService rewardsService;

    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    /**
     * Return all customers' reward summaries.
     *
     * @return 200 OK with a JSON array of {@link com.example.rewards.model.RewardSummary}
     */
    @GetMapping
    public ResponseEntity<List<RewardSummary>> all() {
        return ResponseEntity.ok(rewardsService.allCustomerRewards());
    }

    /**
     * Return a single customer's reward summary.
     *
     * @param customerId identifier of the customer
     * @return 200 OK with the summary when found, or 404 Not Found when no such customer exists
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<RewardSummary> byCustomer(@PathVariable String customerId) {
        return rewardsService.allCustomerRewards().stream()
                .filter(r -> r.getCustomerId().equals(customerId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new com.example.rewards.exception.ResourceNotFoundException("Customer '" + customerId + "' not found"));
    }
}
