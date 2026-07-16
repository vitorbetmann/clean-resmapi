# clean-resmapi

Restaurant management system backend — Tech Challenge Fase 2 (FIAP Pós Tech, Arquitetura e Desenvolvimento Java).

A group of restaurants is pooling resources into a single shared management system, letting customers browse restaurants, leave reviews, and order online, while owners manage their own restaurant's data. This phase covers user type management, restaurant registration, and menu item registration, built with Clean Architecture.

> This README is a living document — sections marked `TODO` will be filled in as each vertical slice (`Restaurant`, `MenuItem`) lands.

## Tech stack

- Java 25
- Spring Boot 4.1 (Web MVC, Data JPA, Validation)
- PostgreSQL
- Docker / Docker Compose
- JUnit 5 + Mockito (unit tests)
- Testcontainers (integration tests + local dev database)
- Lombok

## Architecture

The codebase follows Clean Architecture, organized by feature under `com.vitorbetmann.cleanresmapi`:

- **`entities/<feature>`** — framework-free domain objects. Self-validating constructors (throw `IllegalArgumentException` on invalid state), no JPA/Spring/Lombok annotations beyond what's purely structural. A static `create(...)` factory builds a new, unpersisted instance; the public constructor reconstitutes an already-persisted one.
- **`usecases/<feature>`** — one class per application operation (e.g. `CreateUserType`), each exposing a single `execute(...)` method. Use cases depend only on a `<Feature>Gateway` interface, never on a concrete persistence implementation.
- **`usecases/<feature>/interfaces`** — the `<Feature>Gateway` port each use case depends on.
- **`usecases/<feature>/exceptions`** — domain-specific exceptions (e.g. `UserTypeNotFoundException`), extending shared base exceptions in `usecases/exceptions`.
- **`infrastructure/<feature>`** — the outer layer: JPA entities, Spring Data repositories, `<Feature>GatewayImpl` (implements the gateway port), `@Configuration` classes wiring use cases as beans, DTOs, and the REST controller.

The Dependency Rule applies throughout: inner layers (`entities`, `usecases`) never import from outer layers (`infrastructure`, Spring, JPA). Outer layers depend inward, never the reverse.

> **Note on `lastModifiedDate`:** `User` carries a `lastModifiedDate` field because that was a spec requirement carried over from Fase 1. It's not a requirement for this phase's other entities, so `UserType`, `Restaurant`, and `MenuItem` intentionally don't have one — this is an asymmetry by design, not an oversight.

### Vertical slices

| Feature | Status |
|---|---|
| `UserType` | Done — domain, use cases, tests, infrastructure, controller, integration-tested |
| `User` | Done — domain, use cases, tests, infrastructure, controller, integration-tested |
| `Restaurant` | Done — domain, use cases, tests, infrastructure, controller, integration-tested |
| `MenuItem` | Not started |

## Running the app

### Option 1 — Testcontainers-managed Postgres (no external setup)

Run `TestCleanResmapiApplication.main()`. It boots `CleanResmapiApplication` with `TestcontainersConfiguration` imported, so Docker spins up a disposable Postgres container automatically — no manual database setup needed.

### Option 2 — Docker Compose Postgres + app from your IDE

1. Start Postgres: `docker compose up -d` (uses `compose.yaml` at the project root).
2. Run `CleanResmapiApplication` from your IDE or via `./mvnw spring-boot:run`. It connects to Postgres at `localhost:5432` per `application.properties`.

> TODO: once the app itself is containerized (see task list), add a fully containerized "everything via `docker compose up`" option here.

## Building and testing

- Compile: `./mvnw compile`
- Run all tests: `./mvnw test`
- Run a single test class: `./mvnw test -Dtest=UserTypeTest`
- Run a single test method: `./mvnw test -Dtest=UserTypeTest#constructor_whenNameIsValid_returnsNewUserType`
- Package: `./mvnw package`

Tests that hit the database rely on Testcontainers, which requires a running Docker daemon.

## API Endpoints

### UserType (`/user-types`)

| Method | Path | Description | Success | Failure |
|---|---|---|---|---|
| POST | `/user-types` | Create a user type | 201 | 409 (duplicate name) |
| GET | `/user-types` | List all user types | 200 | — |
| GET | `/user-types/{id}` | Get a user type by id | 200 | 404 |
| PUT | `/user-types/{id}` | Update a user type | 200 | 404, 409 |
| DELETE | `/user-types/{id}` | Delete a user type | 204 | 404 |

### User (`/users`)

| Method | Path | Description | Success | Failure |
|---|---|---|---|---|
| POST | `/users` | Create a user | 201 | 400 (validation), 404 (unknown `userTypeId`), 409 (duplicate email) |
| GET | `/users` | List all users | 200 | — |
| GET | `/users/{id}` | Get a user by id | 200 | 404 |
| PUT | `/users/{id}` | Update a user | 200 | 400, 404, 409 |
| DELETE | `/users/{id}` | Delete a user | 204 | 404 |

### Restaurant (`/restaurants`)

| Method | Path | Description | Success | Failure |
|---|---|---|---|---|
| POST | `/restaurants` | Create a restaurant | 201 | 400 (validation, or owner is not of type Owner), 404 (unknown `ownerId`) |
| GET | `/restaurants` | List all restaurants | 200 | — |
| GET | `/restaurants/{id}` | Get a restaurant by id | 200 | 404 |
| PUT | `/restaurants/{id}` | Update a restaurant | 200 | 400, 404 |
| DELETE | `/restaurants/{id}` | Delete a restaurant | 204 | 404 |

No uniqueness constraint on `name` — a restaurant's name is a display label, not an identifier, so no 409 path here (see `RestaurantGateway`).

### MenuItem

> TODO.

## Postman / API collection

> TODO: link or include the collection once built.

## Docker Compose

> TODO: document the full `docker compose up` workflow once the app service is added to `compose.yaml`.
