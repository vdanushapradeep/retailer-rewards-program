package com.example.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Retailer Rewards Spring Boot application.
 *
 * The application exposes a small REST API to demonstrate reward points
 * calculation and aggregation. This class boots the Spring context and
 * starts the embedded web server.
 */
@SpringBootApplication
public class RewardsApplication {
    /**
     * Main method used when launching the application from the command line.
     *
     * @param args command-line arguments forwarded to SpringApplication
     */
    public static void main(String[] args) {
        SpringApplication.run(RewardsApplication.class, args);
    }
}
