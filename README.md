# NextPoint
[![NextPoint API Tests](https://github.com/julius-kom/nextpoint/actions/workflows/api-tests.yml/badge.svg)](https://github.com/julius-kom/nextpoint/actions/workflows/api-tests.yml)

NextPoint is a travel booking service created as a portfolio project for API and integration test automation.

The repository contains both the Spring Boot application under test and a separate automated testing module that interacts with the application through real HTTP requests.

## Tech Stack

### Application
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Keycloak / OAuth 2.0 / JWT
- Maven
- Docker Compose

### Test Automation
- Java 21
- JUnit
- REST Assured
- AssertJ
- Allure
- WireMock
- PostgreSQL JDBC
- RabbitMQ Java Client
- Maven

## Architecture

The project is organized as two independent modules:

- `app` — Spring Boot backend application under test
- `tests` — API automation framework

The automated tests do not access backend classes directly. They interact with NextPoint through HTTP APIs and verify the behavior of the running application and its integrations.

External infrastructure is started with Docker Compose:

- PostgreSQL — application database
- WireMock — payment service mock
- RabbitMQ — asynchronous booking events
- Keycloak — authentication and authorization

## API

### Trips
- `POST /api/trips` — create a trip
- `GET /api/trips` — get available trips
- `GET /api/trips/{id}` — get trip by ID
- `PUT /api/trips/{id}` — update a trip

### Bookings
- `POST /api/bookings` — create a booking
- `GET /api/bookings/{id}` — get booking by ID
- `PATCH /api/bookings/{id}/cancel` — cancel a booking

## Automated Test Coverage

The automated test suite covers:

- trip creation and validation
- trip update and retrieval
- booking creation
- total price calculation
- available seat updates
- booking cancellation
- idempotent repeated cancellation
- insufficient seats
- booking a nonexistent trip
- booking a past trip
- database verification
- payment success and decline
- payment service errors and timeouts
- RabbitMQ `BOOKING_CREATED` events
- absence of booking events after failed payment
- JWT authentication
- role-based authorization
- `401 Unauthorized` and `403 Forbidden` scenarios

## Payment Service Testing

The payment service is treated as an external dependency and is simulated with WireMock.

Tests can configure different payment scenarios, including:

- successful payment
- declined payment
- HTTP 5xx response
- response timeout

The tests also verify requests sent by NextPoint to the payment service.

## Authentication and Authorization

NextPoint uses Keycloak as an identity provider.

Two application roles are used:

- `ADMIN` — can create and update trips
- `TRAVELER` — can browse trips and manage bookings

The backend acts as an OAuth 2.0 Resource Server and validates JWT access tokens issued by Keycloak.

## Test Isolation

Tests create and use their own business entities instead of globally clearing the database.

WireMock mappings and the RabbitMQ test queue are reset before each test to keep test scenarios isolated and reproducible.

## Continuous Integration

The project uses GitHub Actions for continuous integration.

On every push or pull request to `main`, the CI pipeline:

- sets up Java 21
- starts PostgreSQL, WireMock, RabbitMQ, and Keycloak with Docker Compose
- waits for Keycloak to become ready
- starts the NextPoint Spring Boot application
- waits for the application API to become available
- runs the complete API automation test suite
- uploads Allure test results as a build artifact

Service readiness checks use bounded retries, so the pipeline fails with diagnostic information instead of waiting indefinitely if a service cannot start.

## Running Locally

### Prerequisites

- Java 21
- Maven
- Docker Desktop

### Start infrastructure

From the repository root:
```bash
docker compose up -d
```

Wait until Keycloak is fully initialized before running the application or automated tests.

### Start the application

Run `AppApplication` from the `app` module.

The application is available at `http://localhost:8080`.

### Run automated tests

From the `tests` directory:
```bash
mvn test
```

### Stop infrastructure

From the repository root:
```bash
docker compose down
```

## Reports

Allure is used for test reporting.

REST Assured requests and responses are attached to Allure reports for API test scenarios. Authentication requests containing test credentials are excluded from HTTP report attachments.

## Security Considerations

- Authentication is handled by Keycloak rather than storing user passwords in the application.
- API access is protected using JWT authentication and role-based authorization.
- Test credentials included in the repository are intended only for the local Docker-based test environment.
- Authentication HTTP requests are excluded from Allure attachments to avoid exposing credentials and access tokens.
- Database checks use parameterized JDBC queries.
- External payment failures are mapped to controlled API responses rather than exposing internal exceptions.