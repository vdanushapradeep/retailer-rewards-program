package com.example.rewards.controller;

import com.example.rewards.RewardsApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Consolidated tests for {@link RewardsController} combining the previous
 * unit-style and integration-style checks into a single test class.
 */
@SpringBootTest(classes = RewardsApplication.class)
@AutoConfigureMockMvc
@DisplayName("RewardsController combined tests")
public class RewardsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("GET /api/rewards returns 200")
    void getAllRewards() throws Exception {
        mvc.perform(get("/api/rewards"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/rewards returns 200")
    void getAllRewardsPerCustomer() throws Exception {
        mvc.perform(get("/api/rewards/2"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/rewards/{id} returns 404 for missing customer")
    void getMissingCustomer() throws Exception {
        mvc.perform(get("/api/rewards/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST to GET-only endpoint returns 405")
    void postToGetEndpointReturns405() throws Exception {
        mvc.perform(post("/api/rewards"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Malformed customer id path returns 4xx")
    void malformedCustomerIdPath() throws Exception {
        mvc.perform(get("/api/rewards/%20%20"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("404 Not Found returns standardized error JSON")
    void notFoundReturnsErrorPayload() throws Exception {
        mvc.perform(get("/api/rewards/999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.error", equalTo("Not Found")))
                .andExpect(jsonPath("$.message", equalTo("Customer not found: 999")))
                .andExpect(jsonPath("$.path", equalTo("/api/rewards/999")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }
}
