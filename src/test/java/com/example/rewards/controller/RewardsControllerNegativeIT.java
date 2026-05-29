package com.example.rewards.controller;

import com.example.rewards.RewardsApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Negative/invalidation tests for the REST API.
 *
 * These tests ensure the application responds with appropriate 4xx status codes
 * when clients make unsupported or malformed requests.
 */
@SpringBootTest(classes = RewardsApplication.class)
@AutoConfigureMockMvc
@DisplayName("RewardsController negative tests")
public class RewardsControllerNegativeIT {

    @Autowired
    private MockMvc mvc;

    /**
     * Verify that POSTing to a GET-only endpoint results in 405 Method Not Allowed.
     */
    @Test
    @DisplayName("POST to GET-only endpoint returns 405")
    void postToGetEndpointReturns405() throws Exception {
        mvc.perform(post("/api/rewards"))
                .andExpect(status().isMethodNotAllowed());
    }

    /**
     * Verify malformed customer id paths produce a 4xx response (typically 404).
     */
    @Test
    @DisplayName("Malformed customer id path returns 4xx")
    void malformedCustomerIdPath() throws Exception {
        // a path with spaces or encoded characters should be treated as invalid
        mvc.perform(get("/api/rewards/%20%20"))
                .andExpect(status().is4xxClientError());
    }
}
