# Cricket Academy Management

A production-ready, microservices-based Cricket Academy Management system.

## Tech Stack
- Java 17, Spring Boot 3+, Spring Cloud (Eureka, Gateway), MySQL
- Vue.js 3 + Vite (frontend)
- Docker & Docker Compose

## Modules
- **gateway-service**: API Gateway (Spring Cloud Gateway, OpenAPI/Swagger, global CORS)
- **discovery-service**: Eureka Service Registry
- **auth-service**: JWT authentication, role-based access, role impersonation
- **player-service**: Player CRUD, attendance tracking
- **coach-service**: Coach CRUD, batch mapping
- **batch-service**: Batch creation, scheduling, mapping
- **fee-service**: Fee setup, payment tracking, receipts
- **notification-service**: Email/SMS notifications
- **frontend**: Vue.js 3 SPA with role-based dashboards

## Running Locally

1. **Build all services:**
   ```sh
   mvn clean package -DskipTests
   ```

2. **Start with Docker Compose:**
   ```sh
   docker-compose up --build
   ```

3. **Access:**
   - Eureka Dashboard: [http://localhost:8761](http://localhost:8761)
   - API Gateway: [http://localhost:8080](http://localhost:8080)
   - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - Frontend: [http://localhost:5173](http://localhost:5173)

## Environment Variables

- DB_HOST, DB_PORT, DB_USER, DB_PASSWORD for database config
- EUREKA_HOST, EUREKA_PORT for service discovery
- JWT_SECRET for auth-service JWT signing
- MAIL_HOST, MAIL_PORT, MAIL_USER, MAIL_PASSWORD for notification-service

## Role Switch Feature

- **Endpoint:** `GET /auth/impersonate?role=ROLE_X` (roles: ROLE_ADMIN, ROLE_SUPPORT, ROLE_COACH, ROLE_PLAYER)
- **Usage:**
  - Admin can impersonate Support or Coach
  - Support can impersonate Coach
  - Returns a JWT for the selected role
  - Use the token in the frontend to switch dashboard context

## API Endpoints (Sample)

- `/auth/impersonate?role=ROLE_X` – Get JWT for selected role
- `/players/hello` – Player service sample endpoint
- `/coaches/hello` – Coach service sample endpoint
- `/batches/hello` – Batch service sample endpoint
- `/fees/hello` – Fee service sample endpoint
- `/notifications/hello` – Notification service sample endpoint

## Production Readiness

- All configs support environment variables
- Use Docker for consistent deployment
- Each service follows best practices for package structure: `com.bt.cricketacademy.<service>`
- OpenAPI/Swagger docs available at `/swagger-ui.html` via gateway
- Global CORS enabled in gateway

---

**You can now build and run the entire system with Docker Compose!**
