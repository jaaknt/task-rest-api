# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Task REST API is a Spring Boot 3.5.7 application providing REST endpoints for task and user management. The application uses PostgreSQL (via Docker) for persistence, Liquibase for database migrations, and Lombok for boilerplate reduction.

**Technology Stack**: Java 21, Spring Boot 3.5.7, Gradle (Kotlin DSL), PostgreSQL 17, Liquibase, Lombok, SpringDoc OpenAPI

## Essential Commands

### Development Workflow
```bash
# Start PostgreSQL database (required before running app)
docker-compose up

# Run application locally
./gradlew bootRun

# Run all tests
./gradlew test

# Run tests with detailed output
./gradlew test --info

# Run single test class
./gradlew test --tests "com.multimarketing.taskrestapi.controller.TaskControllerTest"

# Run single test method
./gradlew test --tests "com.multimarketing.taskrestapi.controller.TaskControllerTest.testFindTaskById"

# Build application (includes running tests)
./gradlew build

# Clean build artifacts
./gradlew clean
```

### Database Configuration
- **PostgreSQL Port**: 5433 (mapped from container port 5432)
- **Database Schema**: `eagle` (application schema)
- **Database User**: `root` / `secret` (admin), `app` / `app` (application user)
- **Liquibase Migrations**: Located in `src/main/resources/db/changelog/`
- **Init Scripts**: `src/main/resources/init-scripts/01-init.sql` creates the `eagle` schema and `app` user

### API Documentation
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/spring-docs

## Architecture

### Package Structure
```
com.multimarketing.taskrestapi/
├── config/           - SpringDoc/OpenAPI configuration
├── constants/        - Error messages and application constants
├── controller/       - REST controllers (TaskController, UserController)
├── enums/           - Enums (TaskStatus: CREATED, IN_PROGRESS, COMPLETED, CANCELLED)
├── exception/       - Custom exceptions and GlobalExceptionHandler
├── model/           - JPA entities (Task, User)
├── repository/      - Spring Data JPA repositories
└── service/
    └── impl/        - Service implementations (TaskServiceImpl, UserServiceImpl)
```

### Key Design Patterns

**Service Layer Pattern**: Controllers delegate all business logic to service interfaces with implementations in `service.impl/`

**Exception Handling**:
- `ApiException` for custom business exceptions with HTTP status codes
- `GlobalExceptionHandler` (@ControllerAdvice) handles exceptions globally
- Error messages centralized in `ErrorMessages` constants class

**Database Schema Management**:
- Liquibase changesets are numbered sequentially (1000, 1001, 1002, etc.)
- Schema is `eagle`, Liquibase tracking tables in `public`
- JPA uses `validate` mode - schema changes MUST go through Liquibase migrations
- All database objects (tables, sequences, triggers) created via Liquibase SQL files

**Entity Relationships**:
- Task → User: `@ManyToOne` relationship (many tasks can belong to one user)
- Both entities use database sequences for ID generation (task_id_seq, user_id_seq)

### Configuration Notes

**application.properties Important Settings**:
- `spring.jpa.hibernate.ddl-auto=validate` - Schema changes require Liquibase migrations
- `spring.jpa.properties.hibernate.default_schema=eagle` - All tables in eagle schema
- `spring.datasource.url` points to localhost:5433 (Docker container port mapping)

**Lombok Usage**:
- `@Data` generates getters, setters, toString, equals, hashCode
- `@AllArgsConstructor` for constructor injection in services and controllers
- Entity classes use unusual syntax: `public @Data class Task` (annotation between public and class)

### Testing

**Test Structure**:
- Controller tests: `src/test/java/.../controller/` - Test REST endpoints
- Service tests: `src/test/java/.../service/` - Test business logic with Mockito
- Uses JUnit 5 (Jupiter) and Mockito for mocking

**Test Configuration**:
- Mockito inline agent configured in build.gradle.kts for mocking final classes
- Test reports generated in `build/reports/tests/test/index.html`

## Database Port Configuration

**Critical**: Port configuration details:
- Docker exposes PostgreSQL on host port **5433** (to avoid conflicts with other PostgreSQL instances)
- Application configured to connect to **localhost:5433** in `application.properties`
- Container internal port is 5432 (standard PostgreSQL port)
- If you have multiple PostgreSQL containers running, ensure you're connecting to the correct port

## Common Development Patterns

### Adding New Liquibase Migration
1. Create new SQL file in `src/main/resources/db/changelog/XXXX-description.sql`
2. Add changeSet entry to `changelog-master.yaml` with incremented ID
3. Restart application - Liquibase applies migration automatically

### Creating New Entity
1. Add JPA entity in `model/` package
2. Create repository interface extending `JpaRepository`
3. Create service interface and implementation
4. Create REST controller with OpenAPI annotations
5. Add Liquibase migration for database table

### Exception Handling Pattern
```java
// Throw custom exception with HTTP status
throw new ApiException(ErrorMessages.NOT_FOUND_TASK_BY_ID + id, HttpStatus.NOT_FOUND);

// GlobalExceptionHandler automatically converts to proper HTTP response
```

### OpenAPI Documentation Pattern
- Use `@Tag` on controller class for grouping
- Use `@Operation(summary = "...")` on controller methods
- Use `@ApiResponses` to document all possible response codes
- Annotations enable interactive testing via Swagger UI
