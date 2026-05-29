# Retailer Rewards Program

Spring Boot service that calculates customer reward points from retail transactions and exposes a small REST API to retrieve per-customer monthly and total rewards.

## Features

- Calculate reward points per transaction using the rules:
	- 1 point per dollar for each dollar spent over $50 and up to $100
	- 2 points per dollar for each dollar spent over $100
- Aggregate points per customer by month and total across months
- REST API: list all customers' reward summaries and fetch a single customer's summary
- Sample data loader and unit/integration tests included

## Prerequisites

- Java 17 or later
- Maven 3.6+ (3.8+ recommended)
- PORT 8080 free (configurable via Spring properties)

## Build & Run

Build the project and run with Maven (development):

```powershell
mvn clean install
mvn spring-boot:run
```

The service defaults to http://localhost:8080.

## API

Base path: `/api/rewards`

- GET `/api/rewards`
	- Description: Returns reward summaries for all customers
	- Response: `200 OK` with JSON array of `RewardSummary` objects

- GET `/api/rewards/{customerId}`
	- Description: Returns a single customer's reward summary
	- Response: `200 OK` with `RewardSummary` when found
	- Response: `404 Not Found` when the customer does not exist (throws `ResourceNotFoundException`)

Example request (curl):

```bash
curl http://localhost:8080/api/rewards/123
```

Example response (abridged):

```json
{
	"customerId": "123",
	"monthlyRewards": [
		{ "month": "2023-01", "points": 120 },
		{ "month": "2023-02", "points": 75 }
	],
	"totalPoints": 195
}
```

The `RewardSummary` model contains the customer identifier, a list of monthly point totals (year-month strings), and an overall `totalPoints` value.

## Sample Data

At startup `SampleDataLoader` inserts sample customers and transactions so the API returns data immediately. See [src/main/java/com/example/rewards/data/SampleDataLoader.java](src/main/java/com/example/rewards/data/SampleDataLoader.java).

## Project layout (important files)

- Controller: [src/main/java/com/example/rewards/controller/RewardsController.java](src/main/java/com/example/rewards/controller/RewardsController.java)
- Service: [src/main/java/com/example/rewards/service/RewardsService.java](src/main/java/com/example/rewards/service/RewardsService.java)
- Calculator util: [src/main/java/com/example/rewards/util/RewardCalculator.java](src/main/java/com/example/rewards/util/RewardCalculator.java)
- Models: [src/main/java/com/example/rewards/model/](src/main/java/com/example/rewards/model/)
- Tests: [src/test/java/com/example/rewards/](src/test/java/com/example/rewards/)

## Tests

Run unit and integration tests with:

```powershell
mvn test
```

Integration tests exercise the controller endpoints and error payloads. Unit tests cover the reward calculation logic including edge cases.

## Troubleshooting

- Port already in use: stop the process using 8080 or override with `--server.port=XXXX` when running
- If you see `404 Not Found` for a customer check that the sample data loader created the customer id you requested

---

Small note: controller behavior and endpoints are implemented in the source; see [src/main/java/com/example/rewards/controller/RewardsController.java](src/main/java/com/example/rewards/controller/RewardsController.java) for exact contract and error handling.
