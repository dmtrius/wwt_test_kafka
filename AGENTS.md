# AGENTS.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

Spring Boot microservices demo with two services communicating via Kafka:
- **auth-api** (port 8080): Authentication, JWT tokens, and request orchestration
- **data-api** (port 8081): Stateless text processing worker

## Build & Run

### Build (requires Java 25)
```powershell
mvn -f auth-api/pom.xml clean package -DskipTests
mvn -f data-api/pom.xml clean package -DskipTests
```

### Run full stack with Docker
```powershell
docker compose up -d --build
```

### Run tests (auth-api uses Testcontainers for PostgreSQL)
```powershell
mvn -f auth-api/pom.xml test
mvn -f data-api/pom.xml test
```

## Architecture

### Service Communication Flow
```
Client → auth-api → [Kafka: process-requests] → data-api
                                                    ↓
Client ← auth-api ← [Kafka: process-results] ←────┘
```

1. Client authenticates via `/api/v1/auth/login` → receives JWT
2. Client calls `/api/v1/process` with JWT and text payload
3. auth-api validates JWT, publishes `ProcessEvent` to Kafka topic `process-requests`
4. auth-api waits on `CompletableFuture` (5s timeout) in `ProcessServiceImpl.pending` map
5. data-api consumes event, reverses/uppercases text, publishes `ProcessResultEvent` to `process-results`
6. auth-api's `@KafkaListener` completes the future, logs to PostgreSQL, returns response

### Key Components

**auth-api:**
- `JwtFilter` + `JwtUtil`: JWT authentication via JJWT library
- `SecurityConfig`: Permits `/api/*/auth/**` and Swagger, authenticates everything else
- `ProcessServiceImpl`: Kafka producer/consumer with correlation via UUID → CompletableFuture map
- `ProcessingLogRepository`: JPA logging of processed requests

**data-api:**
- `ProcessConsumer`: Kafka listener that transforms text (reverse + uppercase)
- No database - purely event-driven processing

### Kafka Topics
- `process-requests`: auth-api → data-api (ProcessEvent)
- `process-results`: data-api → auth-api (ProcessResultEvent)

### Database
PostgreSQL with schema auto-initialized from `auth-api/src/main/resources/data/schema.sql`

## API Endpoints

- `POST /api/v1/auth/register` - Register user (public)
- `POST /api/v1/auth/login` - Login, returns JWT (public)
- `POST /api/v1/process` - Process text (requires Bearer token)
- `/swagger-ui.html` - API documentation

## Environment Variables

Key variables configured in `docker-compose.yml`:
- `SPRING_DATASOURCE_URL/USERNAME/PASSWORD` - PostgreSQL connection
- `KAFKA_BOOTSTRAP_SERVERS` - Kafka broker address
- `JWT_SECRET` - JWT signing key
- `INTERNAL_TOKEN` - Service-to-service auth token
