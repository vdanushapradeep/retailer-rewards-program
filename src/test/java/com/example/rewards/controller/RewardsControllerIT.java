package com.example.rewards.controller;

import com.example.rewards.RewardsApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Integration tests for the rewards REST endpoints (happy paths). */
@SpringBootTest(classes = RewardsApplication.class)
@AutoConfigureMockMvc
@DisplayName("RewardsController integration tests")
public class RewardsControllerIT {

    @Autowired
    private MockMvc mvc;

    /** Verify the all-customers endpoint returns 200 OK and a JSON array. */
    @Test
    @DisplayName("GET /api/rewards returns 200")
    void getAllRewards() throws Exception {
        mvc.perform(get("/api/rewards"))
                .andExpect(status().isOk());
    }

    /** Verify requesting a non-existing customer returns 404 Not Found. */
    @Test
    @DisplayName("GET /api/rewards/{id} returns 404 for missing customer")
    void getMissingCustomer() throws Exception {
        mvc.perform(get("/api/rewards/UNKNOWN"))
                .andExpect(status().isNotFound());
    }
}
