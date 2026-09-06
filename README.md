# DreamCart — Backend

The REST API powering **DreamCart**, a full-stack e-commerce platform. Built with Spring Boot and PostgreSQL, it handles authentication, product catalog, cart, wishlist, checkout, payments, order management, product reviews, and admin operations.

Frontend repo: [dreamcart-frontend](https://github.com/sushilranavare/dreamcart-frontend)

## Tech Stack

- Java 21, Spring Boot 3
- Spring Security with stateless JWT authentication
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- springdoc-openapi (Swagger UI)
- Spring Mail (order confirmation emails, sent asynchronously)
- JUnit 5 + Mockito (unit tests)
- Docker / Docker Compose

## Features

- JWT-based authentication and registration, with role-based access control (`USER` / `ADMIN`) enforced via `@PreAuthorize`
- Product and category browsing (public), with image upload support
- Cart and wishlist management
- Checkout flow with shipping addresses and a simulated payment step
- Order placement with pessimistic row locking on stock deduction, so concurrent orders can't oversell inventory
- Order history for users, and status management for admins
- Product reviews (one per user per product), with average rating
- User profile management (update details, change password)
- Admin dashboard: store statistics, and management of products, categories, users, and orders
- API documentation via Swagger UI
- Centralized exception handling with consistent JSON error responses

## Architecture

```mermaid
graph TB
    Browser["Browser"]

    subgraph FE["Frontend container - Nginx :5173"]
        SPA["React + Vite<br/>static build"]
    end

    subgraph BE["Backend container - Spring Boot :8080"]
        Filter["JwtAuthenticationFilter<br/>+ Spring Security"]
        Controllers["REST Controllers"]
        Services["Service layer"]
        Static["Static product images<br/>/uploads/products/**"]
    end

    DB[("PostgreSQL<br/>dreamcart_db")]
    SMTP["SMTP<br/>order confirmation"]

    Browser -->|"loads SPA"| SPA
    SPA -->|"Axios<br/>Authorization: Bearer JWT"| Filter
    SPA -->|"img src - no JWT"| Static
    Filter --> Controllers
    Controllers --> Services
    Services --> DB
    Filter -.->|"loads user<br/>every request"| DB
    Services -.->|"@Async"| SMTP
```

## Key Flows

### Login and authenticated requests

```mermaid
sequenceDiagram
    participant U as User
    participant F as Frontend
    participant B as Backend
    participant D as PostgreSQL

    U->>F: Submit email + password
    F->>B: POST /api/auth/login
    Note over B: JWT filter is skipped<br/>for /login and /register
    B->>D: findByEmail
    D-->>B: User with hashed password
    B->>B: BCrypt matches, then<br/>generate JWT with email as subject
    B-->>F: token, role, message
    F->>F: Store token + role in localStorage
    F-->>U: Redirect to home

    Note over F,B: Every authenticated request afterwards
    F->>B: Authorization: Bearer token
    B->>D: Load user by email from token
    B->>B: Validate token, set SecurityContext
    B-->>F: Protected resource
```

### Checkout and payment

```mermaid
sequenceDiagram
    participant U as User
    participant F as Frontend
    participant B as Backend
    participant D as PostgreSQL
    participant M as SMTP

    U->>F: Click "Place Order"

    F->>B: POST /api/orders with addressId
    B->>D: Verify address belongs to user
    B->>D: Load cart items
    loop For each cart item
        B->>D: SELECT product FOR UPDATE (pessimistic lock)
        B->>B: Check stock, else reject
        B->>D: Deduct stock
    end
    B->>D: Save Order (status PLACED) + OrderItems
    B->>D: Clear cart items
    B-->>F: Order with id and totalAmount

    F->>B: POST /api/payments with orderId + paymentMethod
    B->>D: Reject if payment already exists
    B->>D: Save Payment (SUCCESS, transactionId)
    B->>D: Update Order status to CONFIRMED
    B-->>F: Payment details
    B-)M: Order confirmation email (@Async)

    F-->>U: Success alert, redirect to /orders
```

## Prerequisites

- Java 21
- Maven (or use the included `./mvnw` wrapper)
- PostgreSQL 16 (or run everything via Docker instead — see below)

## Running Locally

1. Create a PostgreSQL database named `dreamcart_db`.
2. Update `src/main/resources/application.properties` with your local database credentials if they differ from the defaults.
3. Run the app:
   ```bash
   ./mvnw spring-boot:run
   ```
4. The API is available at `http://localhost:8080`.

## Running with Docker

The easiest way to run the full stack (backend + frontend + PostgreSQL) together:

```bash
docker compose up --build
```

This starts:
- PostgreSQL on host port `5433` (mapped to container port `5432`, to avoid clashing with a local Postgres install)
- Backend on `http://localhost:8080`
- Frontend on `http://localhost:5173`

The compose file assumes `dreamcart-backend` and `dreamcart-frontend` are cloned as sibling directories. See `docker-compose.yml` for details.

## API Documentation

Once the backend is running, interactive API docs are available at:

```
http://localhost:8080/swagger-ui.html
```

Authenticate via `POST /api/auth/login`, then paste the returned JWT into Swagger UI's "Authorize" button to test protected endpoints directly.

## Testing

Run the backend unit test suite (JUnit 5 + Mockito):

```bash
./mvnw test
```

## Project Structure

```
src/main/java/com/dreamcart/backend/
├── controller/    REST endpoints
├── service/       Business logic
├── repository/    Spring Data JPA repositories
├── entity/        JPA entities
├── dto/           Request/response DTOs
├── security/      JWT filter and Spring Security configuration
├── config/        App-wide configuration (OpenAPI, async, static resources)
└── exceptions/    Global exception handling
```
