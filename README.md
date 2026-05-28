# Retailer Rewards Program

Simple Spring Boot application that calculates reward points for customer transactions.

Features:
- Calculates points per transaction using rules: 1 point per dollar between $50 and $100, 2 points per dollar above $100.
- Aggregates monthly and total points per customer.
- Exposes REST API: GET /api/rewards and GET /api/rewards/{customerId}
- Includes unit and integration tests.

How to run

1. Build and run using Maven:

```powershell
mvn clean package
mvn spring-boot:run
```

2. Endpoints:

- GET /api/rewards -> list of all customers with monthly and total points
- GET /api/rewards/{customerId} -> single customer or 404 if not found

Notes
- Sample data is loaded at startup from `SampleDataLoader`.
- Months are derived from transaction dates using year-month strings (no hardcoded month names).

Tests

Run:

```powershell
mvn test
```
# retailer-rewards-program
Spring Boot Application to calculate customer reward points based on retail transactions over a three-month period. Includes monthly and total reward aggregation, unit/integration testing, exception handling, and clean layered architecture.
