# Kneu Health — Backend Engineering Tech Test

## Time Guidance

This exercise is designed to take no more than **2 hours**. Please try to stop once you reach the 2-hour mark, even if the solution feels incomplete. We're not expecting a finished or polished solution. We care more about:

- How you structure your code
- How you think about trade-offs
- How you explain what you would do next

If you run out of time, please leave comments or notes describing:

- What you would improve next
- Any trade-offs you made
- Anything you intentionally left out

## Context

Kneu Health is building a medication management service. The end goal is an app where patients can manage their prescriptions and record when they've taken their medication.

This starter project includes a PostgreSQL database pre-filled with medications and a read-only medications API backed by Spring Data JPA. Your task is to build a prescriptions API on top of it.

## Getting Started

1. Click **"Use this template"** to create your own copy of this repo

**Prerequisites:** [Java 21](https://adoptium.net/temurin/releases/?version=21), [Docker](https://docs.docker.com/get-docker/)

```bash
# Start the PostgreSQL database
docker compose up -d

# Run the application
./mvnw spring-boot:run

# Run tests
./mvnw test
```

The app starts on **port 8080**. Verify it works:

```bash
curl http://localhost:8080/medications
```

To stop the database:

```bash
docker compose down        # keeps data
docker compose down -v     # removes data (full reset)
```

## What's Provided

- A **PostgreSQL database** (via Docker) with a `medications` table pre-filled with 5 medications
- A `Medication` JPA entity and a `MedicationRepository` (Spring Data `JpaRepository`)
- A `GET /medications` endpoint that returns all medications

Feel free to refactor any of the provided code as you see fit.

## Task

Build an API that allows users to manage their prescriptions. A prescription links to a medication and includes a time of day it should be taken (e.g. "08:00"), and optionally a short nickname for easy reference (e.g. "morning heart pill").

Users should be able to:

- Create a new prescription
- View prescriptions

### Business Rules

- A prescription must reference a valid medication

## What We Value

- **Separation of concerns** — thoughtful organisation of responsibilities
- **Good API design** — proper use of HTTP methods, status codes, and request/response handling
- **Code quality** — clear naming, readability, and organisation
- **Testing** — meaningful unit and/or integration tests
- **Error handling** — sensible behaviour for edge cases

Feel free to add anything else you think demonstrates good engineering practice.

## Submission

Please share a link to your repository when you're done. Include any notes about your approach or trade-offs if you'd like.
