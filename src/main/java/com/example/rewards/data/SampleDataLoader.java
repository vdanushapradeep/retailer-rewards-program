package com.example.rewards.data;

import com.example.rewards.model.Customer;
import com.example.rewards.model.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads a small, deterministic sample dataset into memory at application startup.
 *
 * Purpose:
 * - Provide a simple dataset for manual testing and automated tests without
 *   requiring external dependencies (database, files etc.).
 * - Keep data relative to {@link LocalDate#now()} so the sample spans multiple
 *   recent months automatically.
 */
@Component
public class SampleDataLoader implements CommandLineRunner {
    // simple in-memory stores used by the service layer and tests
    public static final Map<String, Customer> CUSTOMERS = new HashMap<>();
    public static final List<Transaction> TRANSACTIONS = new ArrayList<>();

    @Override
    public void run(String... args) throws Exception {
        // create customers
        CUSTOMERS.put("C1", new Customer("C1", "Alice"));
        CUSTOMERS.put("C2", new Customer("C2", "Bob"));
        CUSTOMERS.put("C3", new Customer("C3", "Charlie"));

        // create transactions across 3 months relative to now
        LocalDate now = LocalDate.now();
        TRANSACTIONS.add(new Transaction("C1", 120.0, now.minusDays(10)));
        TRANSACTIONS.add(new Transaction("C1", 75.0, now.minusDays(40)));
        TRANSACTIONS.add(new Transaction("C1", 45.0, now.minusDays(70)));

        TRANSACTIONS.add(new Transaction("C2", 200.0, now.minusDays(5)));
        TRANSACTIONS.add(new Transaction("C2", 99.99, now.minusDays(35)));

        TRANSACTIONS.add(new Transaction("C3", 50.0, now.minusDays(15)));
        TRANSACTIONS.add(new Transaction("C3", 130.0, now.minusDays(65)));
    }
}
