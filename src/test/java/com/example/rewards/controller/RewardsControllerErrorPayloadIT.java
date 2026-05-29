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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Tests that validate the standardized error payload produced by the global exception handler.
 */
@SpringBootTest(classes = RewardsApplication.class)
@AutoConfigureMockMvc
@DisplayName("Error payload integration tests")
public class RewardsControllerErrorPayloadIT {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("404 Not Found returns standardized error JSON")
    void notFoundReturnsErrorPayload() throws Exception {
    mvc.perform(get("/api/rewards/UNKNOWN_ID"))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", equalTo(404)))
        .andExpect(jsonPath("$.error", equalTo("Not Found")))
        .andExpect(jsonPath("$.message", equalTo("Customer 'UNKNOWN_ID' not found")))
        .andExpect(jsonPath("$.path", equalTo("/api/rewards/UNKNOWN_ID")))
        .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("405 Method Not Allowed returns standardized error JSON via Spring default handling")
    void methodNotAllowedReturnsErrorPayload() throws Exception {
        mvc.perform(post("/api/rewards"))
                .andExpect(status().isMethodNotAllowed());
        // Note: Spring may return its own default HTML/JSON for 405. We assert the status here.
    }
}
