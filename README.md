# Clean Resmapi — Restaurant Management API

- [🇨🇦 English Description](#english-description)

- [🇧🇷 Descrição em Português](#descrição-em-português)

---

## 🇨🇦English Description

Backend developed for the **Tech Challenge — Phase 02** of the FIAP postgraduate program (Pós Tech, Arquitetura e
Desenvolvimento Java). The challenge proposes a shared management system for restaurants, where owners can run their
operations and customers can browse, review, and place orders online. This second phase migrates the domain to
**Clean Architecture** and delivers three vertical slices — **user type management**, **restaurant registration**,
and **menu item registration** — on top of **user management**, carried over and re-architected from Phase 1.

## Stack

- Java 25
- Spring Boot 4.1 (Web MVC, Data JPA, Validation)
- PostgreSQL
- springdoc-openapi (Swagger UI)
- Lombok
- Maven
- Docker / Docker Compose
- JUnit 5 + Mockito (unit tests)
- Testcontainers (integration tests + local dev database)

## How to run

The application is fully dockerized. The only requirements are Docker and Docker Compose.

### Start the containers

```bash
cp .env.example .env   # first time only — working defaults are already provided
docker compose up --build
```

This launches two services:

- `db` — PostgreSQL 18, with a `pg_isready` healthcheck
- `app` — Spring Boot application exposed at `http://localhost:8080`

The `app` service waits for the `db` healthcheck before starting, so it never races a database that isn't ready to
accept connections yet.

Two lighter-weight alternatives exist for local development — running against a Testcontainers-managed disposable
Postgres, or against just the `db` service from your IDE (`docker compose up -d db`) — see `compose.yaml` and
`TestcontainersConfiguration` in the repo.

### API Documentation

`http://localhost:8080/swagger-ui.html`

Raw OpenAPI spec: `http://localhost:8080/v3/api-docs` (JSON) or `/v3/api-docs.yaml`.

## Endpoints

Unlike Phase 1, there's no URL versioning prefix — each resource is mounted at its own root path.

### UserType — `/user-types`

| Method | Path | Description |
|---|---|---|
| POST | `/user-types` | Registers a new user type |
| GET | `/user-types` | Lists all user types |
| GET | `/user-types/{id}` | Gets a user type by id |
| PUT | `/user-types/{id}` | Updates a user type |
| DELETE | `/user-types/{id}` | Deletes a user type |

### User — `/users`

| Method | Path | Description |
|---|---|---|
| POST | `/users` | Registers a new user (references a `userTypeId`) |
| GET | `/users` | Lists all users |
| GET | `/users/{id}` | Gets a user by id |
| PUT | `/users/{id}` | Updates a user |
| DELETE | `/users/{id}` | Deletes a user |

### Restaurant — `/restaurants`

| Method | Path | Description |
|---|---|---|
| POST | `/restaurants` | Registers a new restaurant (`ownerId` must be a `User` of type `Owner`) |
| GET | `/restaurants` | Lists all restaurants |
| GET | `/restaurants/{id}` | Gets a restaurant by id |
| PUT | `/restaurants/{id}` | Updates a restaurant |
| DELETE | `/restaurants/{id}` | Deletes a restaurant |

### MenuItem — `/menu-items`

| Method | Path | Description |
|---|---|---|
| POST | `/menu-items` | Registers a new menu item (references a `restaurantId`) |
| GET | `/menu-items` | Lists all menu items |
| GET | `/menu-items/{id}` | Gets a menu item by id |
| PUT | `/menu-items/{id}` | Updates a menu item |
| DELETE | `/menu-items/{id}` | Deletes a menu item |

### Example — create the full chain

```http
POST /user-types
Content-Type: application/json

{
  "name": "Owner"
}
```

```http
POST /users
Content-Type: application/json

{
  "name": "Mauricio Borges",
  "email": "mauricio@example.com",
  "password": "secret123",
  "userTypeId": 1,
  "address": "Av. Paulista, 1000"
}
```

```http
POST /restaurants
Content-Type: application/json

{
  "name": "Cantina da Praça",
  "address": "123 Main St",
  "cuisineType": "Italian",
  "openingHours": "09:00-22:00",
  "ownerId": 1
}
```

```http
POST /menu-items
Content-Type: application/json

{
  "name": "Spaghetti Carbonara",
  "description": "Classic Roman pasta with egg, cheese, and pancetta.",
  "price": 28.90,
  "availableOnlyAtRestaurant": true,
  "photoPath": "/photos/spaghetti-carbonara.jpg",
  "restaurantId": 1
}
```

`ownerId` must reference a `User` whose `userType` is `"Owner"` — passing a `Customer`'s id fails with `400`.

### Error model (RFC 7807)

Errors follow the standard `ProblemDetail` format (RFC 7807):

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "UserType ID not found: 999999"
}
```

| Exception family | Status |
|---|---|
| `NotFoundException` (`UserTypeNotFoundException`, `UserNotFoundException`, `RestaurantNotFoundException`, `MenuItemNotFoundException`) | 404 |
| `DuplicateFieldException` (`UserTypeDuplicateNameException`, `UserDuplicateEmailException`) | 409 |
| `IllegalArgumentException` (entity-level rule violations — e.g. a non-`Owner` user assigned as a restaurant's owner, or a non-positive `price`) | 400 |
| Bean Validation failures (`@NotBlank`, `@Email`, `@Positive`, etc. via `@Valid`) | 400 |

All of these are caught centrally by a single `GlobalExceptionHandler`, mapped off two shared base exception classes
(`NotFoundException`, `DuplicateFieldException`) plus a generic `IllegalArgumentException` handler — a new feature
just extends those base classes and gets the correct HTTP status for free, no per-feature handler needed.

## Data model

Unlike Phase 1, which used JPA `SINGLE_TABLE` inheritance for `User`/`Owner`/`Customer`, Phase 2 replaces inheritance
with **composition**: there's a single `User` entity holding a reference to a `UserType` entity (itself just an `id` +
`name`, e.g. `"Owner"`, `"Customer"`), instead of a fixed discriminator column and a rigid subclass hierarchy. Adding
a new user type is now a data change (`POST /user-types`), not a code change.

- **`UserType`** — `id`, `name` (unique)
- **`User`** — `id`, `name`, `email` (unique), `password`, `address`, `userType` (FK), `lastModifiedDate`
- **`Restaurant`** — `id`, `name`, `address`, `cuisineType`, `openingHours`, `owner` (FK to `User`; entity-level rule:
  the referenced `User` must have `userType.name == "Owner"`)
- **`MenuItem`** — `id`, `name`, `description`, `price` (`BigDecimal`, must be `> 0`), `availableOnlyAtRestaurant` (dine-in-only flag), `photoPath` (stored path to the dish photo — no actual file upload/storage, since this is a backend-only service), `restaurant` (FK)

`lastModifiedDate` only exists on `User` — that's a spec requirement carried over from Phase 1, not a pattern the
other entities follow. It's an intentional asymmetry, not an oversight.

## Architecture

The codebase follows Clean Architecture, organized by feature under `com.vitorbetmann.cleanresmapi`:

```
entities/<feature>/              framework-free domain objects, self-validating constructors
usecases/<feature>/              one class per operation (CreateUserType, GetUser, ...), single execute() method
usecases/<feature>/interfaces/   <Feature>Gateway port
usecases/<feature>/exceptions/   domain exceptions extending shared base exceptions
infrastructure/<feature>/        JPA entities, Spring Data repositories, <Feature>GatewayImpl, @Configuration, DTOs, REST controller
```

The Dependency Rule applies throughout: inner layers (`entities`, `usecases`) never import from outer layers
(`infrastructure`, Spring, JPA). Outer layers depend inward, never the reverse. Use cases depend only on a
`<Feature>Gateway` interface — never on a concrete persistence implementation — so swapping JPA/Postgres for
something else would only touch the `infrastructure` layer.

## Test coverage

Coverage is measured with [JaCoCo](https://www.jacoco.org/jacoco/) and enforced via a Maven Failsafe-style gate: `mvn
verify` fails the build if instruction coverage drops below **80%**. Current numbers:

| Metric | Coverage |
|---|---|
| Instructions | 99% (11 of 2,011 missed) |
| Branches | 87% (11 of 90 missed) |
| Lines | 449 total, 3 missed |
| Methods | 173 total, 2 missed |
| Classes | 63 total, 0 missed |

To reproduce locally:

```bash
./mvnw clean verify
open target/site/jacoco/index.html
```

The suite combines JUnit 5 + Mockito unit tests (entities and use cases, no Spring context) with Testcontainers-backed
integration tests (full `MockMvc` → controller → use case → JPA → Postgres round trip) per feature.

## Postman collection

A Postman collection in JSON format is included in the repository at
[clean-resmapi.postman_collection.json](clean-resmapi.postman_collection.json), covering:

Alternatively, to access the collection directly in Postman, click
[here](https://vitorbetmann-5356326.postman.co/workspace/Vitor-Betmann's-Workspace~88c6f948-7512-4287-86ff-a0109b20c44c/collection/49832651-64cb016d-e939-48f0-a740-158eeef7715c?action=share&source=copy-link&creator=49832651)

- Full create → read → update chain across all four resources (`UserType` → `User` → `Restaurant` → `MenuItem`),
  with ids captured automatically between requests
- Valid and invalid registration (duplicate name/email, unknown foreign keys, a non-`Owner` user attempting to own a
  restaurant, a non-positive menu item price)
- Deleting every resource, in FK-safe order, so the collection can be re-run from scratch

Each request asserts its expected status code, so running the whole collection also works as a smoke test against a
live instance.

---

## 🇧🇷Descrição em Português

Backend desenvolvido para o **Tech Challenge — Fase 02** da pós-graduação FIAP (Pós Tech, Arquitetura e
Desenvolvimento Java). O desafio propõe um sistema compartilhado de gestão para restaurantes, em que os
estabelecimentos podem administrar suas operações e os clientes podem consultar informações, avaliar e fazer pedidos
online. Esta segunda fase migra o domínio para **Clean Architecture** e entrega três vertical slices — **gestão de
tipos de usuário**, **cadastro de restaurantes** e **cadastro de itens de cardápio** — além da **gestão de
usuários**, herdada e reformulada a partir da Fase 1.

## Stack

- Java 25
- Spring Boot 4.1 (Web MVC, Data JPA, Validation)
- PostgreSQL
- springdoc-openapi (Swagger UI)
- Lombok
- Maven
- Docker / Docker Compose
- JUnit 5 + Mockito (testes unitários)
- Testcontainers (testes de integração + banco de dados local)

## Como executar

A aplicação é totalmente dockerizada. Os requisitos são apenas Docker e Docker Compose.

### Iniciar os containers

```bash
cp .env.example .env   # apenas na primeira vez — os valores padrão já funcionam
docker compose up --build
```

Isso sobe dois serviços:

- `db` — PostgreSQL 18, com healthcheck via `pg_isready`
- `app` — aplicação Spring Boot exposta em `http://localhost:8080`

O serviço `app` aguarda o healthcheck do `db` antes de iniciar, evitando disputar acesso a um banco que ainda não
está pronto para aceitar conexões.

Duas alternativas mais leves existem para desenvolvimento local — rodar contra um Postgres descartável gerenciado
pelo Testcontainers, ou contra apenas o serviço `db` a partir da sua IDE (`docker compose up -d db`) — veja
`compose.yaml` e `TestcontainersConfiguration` no repositório.

### Documentação da API

`http://localhost:8080/swagger-ui.html`

Especificação OpenAPI bruta: `http://localhost:8080/v3/api-docs` (JSON) ou `/v3/api-docs.yaml`.

## Endpoints

Diferente da Fase 1, não há prefixo de versionamento na URL — cada recurso é montado na sua própria raiz.

### UserType — `/user-types`

| Método | Caminho | Descrição |
|---|---|---|
| POST | `/user-types` | Cadastra um novo tipo de usuário |
| GET | `/user-types` | Lista todos os tipos de usuário |
| GET | `/user-types/{id}` | Busca um tipo de usuário pelo id |
| PUT | `/user-types/{id}` | Atualiza um tipo de usuário |
| DELETE | `/user-types/{id}` | Exclui um tipo de usuário |

### User — `/users`

| Método | Caminho | Descrição |
|---|---|---|
| POST | `/users` | Cadastra um novo usuário (referencia um `userTypeId`) |
| GET | `/users` | Lista todos os usuários |
| GET | `/users/{id}` | Busca um usuário pelo id |
| PUT | `/users/{id}` | Atualiza um usuário |
| DELETE | `/users/{id}` | Exclui um usuário |

### Restaurant — `/restaurants`

| Método | Caminho | Descrição |
|---|---|---|
| POST | `/restaurants` | Cadastra um novo restaurante (`ownerId` deve ser um `User` do tipo `Owner`) |
| GET | `/restaurants` | Lista todos os restaurantes |
| GET | `/restaurants/{id}` | Busca um restaurante pelo id |
| PUT | `/restaurants/{id}` | Atualiza um restaurante |
| DELETE | `/restaurants/{id}` | Exclui um restaurante |

### MenuItem — `/menu-items`

| Método | Caminho | Descrição |
|---|---|---|
| POST | `/menu-items` | Cadastra um novo item de cardápio (referencia um `restaurantId`) |
| GET | `/menu-items` | Lista todos os itens de cardápio |
| GET | `/menu-items/{id}` | Busca um item de cardápio pelo id |
| PUT | `/menu-items/{id}` | Atualiza um item de cardápio |
| DELETE | `/menu-items/{id}` | Exclui um item de cardápio |

### Exemplo — criando a cadeia completa

```http
POST /user-types
Content-Type: application/json

{
  "name": "Owner"
}
```

```http
POST /users
Content-Type: application/json

{
  "name": "Mauricio Borges",
  "email": "mauricio@example.com",
  "password": "secret123",
  "userTypeId": 1,
  "address": "Av. Paulista, 1000"
}
```

```http
POST /restaurants
Content-Type: application/json

{
  "name": "Cantina da Praça",
  "address": "123 Main St",
  "cuisineType": "Italian",
  "openingHours": "09:00-22:00",
  "ownerId": 1
}
```

```http
POST /menu-items
Content-Type: application/json

{
  "name": "Spaghetti Carbonara",
  "description": "Classic Roman pasta with egg, cheese, and pancetta.",
  "price": 28.90,
  "availableOnlyAtRestaurant": true,
  "photoPath": "/photos/spaghetti-carbonara.jpg",
  "restaurantId": 1
}
```

`ownerId` precisa referenciar um `User` cujo `userType` seja `"Owner"` — passar o id de um `Customer` falha com `400`.

### Modelo de erro (RFC 7807)

Erros são padronizados via `ProblemDetail` (RFC 7807):

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "UserType ID not found: 999999"
}
```

| Família de exceção | Status |
|---|---|
| `NotFoundException` (`UserTypeNotFoundException`, `UserNotFoundException`, `RestaurantNotFoundException`, `MenuItemNotFoundException`) | 404 |
| `DuplicateFieldException` (`UserTypeDuplicateNameException`, `UserDuplicateEmailException`) | 409 |
| `IllegalArgumentException` (violação de regra no nível da entidade — ex.: um usuário que não é `Owner` sendo atribuído como dono de um restaurante, ou um `price` não positivo) | 400 |
| Falhas de Bean Validation (`@NotBlank`, `@Email`, `@Positive`, etc. via `@Valid`) | 400 |

Todas essas exceções são tratadas centralmente por um único `GlobalExceptionHandler`, mapeado a partir de duas
classes base compartilhadas (`NotFoundException`, `DuplicateFieldException`) mais um handler genérico para
`IllegalArgumentException` — uma nova feature basta estender essas classes base para já ganhar o status HTTP correto,
sem precisar de um handler próprio.

## Modelo de dados

Diferente da Fase 1, que usava herança JPA `SINGLE_TABLE` para `User`/`Owner`/`Customer`, a Fase 2 substitui a herança
por **composição**: existe uma única entidade `User` com uma referência a uma entidade `UserType` (que por sua vez é
apenas um `id` + `name`, ex.: `"Owner"`, `"Customer"`), em vez de uma coluna discriminadora fixa e uma hierarquia
rígida de subclasses. Adicionar um novo tipo de usuário agora é uma mudança de dado (`POST /user-types`), não uma
mudança de código.

- **`UserType`** — `id`, `name` (único)
- **`User`** — `id`, `name`, `email` (único), `password`, `address`, `userType` (FK), `lastModifiedDate`
- **`Restaurant`** — `id`, `name`, `address`, `cuisineType`, `openingHours`, `owner` (FK para `User`; regra no nível
  da entidade: o `User` referenciado precisa ter `userType.name == "Owner"`)
- **`MenuItem`** — `id`, `name`, `description`, `price` (`BigDecimal`, precisa ser `> 0`), `availableOnlyAtRestaurant`
  (indica se o item só pode ser pedido no consumo local), `photoPath` (caminho onde a foto do prato estaria
  armazenada — sem upload/armazenamento real, já que este é um serviço apenas de back-end), `restaurant` (FK)

`lastModifiedDate` só existe em `User` — isso é um requisito de especificação herdado da Fase 1, não um padrão que as
outras entidades seguem. É uma assimetria intencional, não um esquecimento.

## Arquitetura

O código segue Clean Architecture, organizado por feature sob `com.vitorbetmann.cleanresmapi`:

```
entities/<feature>/              objetos de domínio livres de framework, construtores autovalidados
usecases/<feature>/              uma classe por operação (CreateUserType, GetUser, ...), um único método execute()
usecases/<feature>/interfaces/   a porta <Feature>Gateway
usecases/<feature>/exceptions/   exceções de domínio estendendo exceções base compartilhadas
infrastructure/<feature>/        entidades JPA, repositórios Spring Data, <Feature>GatewayImpl, @Configuration, DTOs, controller REST
```

A Dependency Rule se aplica em todo o código: as camadas internas (`entities`, `usecases`) nunca importam das camadas
externas (`infrastructure`, Spring, JPA). As camadas externas dependem das internas, nunca o contrário. Os use cases
dependem apenas de uma interface `<Feature>Gateway` — nunca de uma implementação concreta de persistência — então
trocar JPA/Postgres por outra coisa afetaria apenas a camada `infrastructure`.

## Cobertura de testes

A cobertura é medida com [JaCoCo](https://www.jacoco.org/jacoco/) e é obrigatória via Maven: o `mvn verify` falha o
build se a cobertura de instruções cair abaixo de **80%**. Números atuais:

| Métrica | Cobertura |
|---|---|
| Instruções | 99% (11 de 2.011 não cobertas) |
| Branches | 87% (11 de 90 não cobertos) |
| Linhas | 449 no total, 3 não cobertas |
| Métodos | 173 no total, 2 não cobertos |
| Classes | 63 no total, 0 não cobertas |

Para reproduzir localmente:

```bash
./mvnw clean verify
open target/site/jacoco/index.html
```

A suíte combina testes unitários com JUnit 5 + Mockito (entidades e use cases, sem contexto Spring) com testes de
integração via Testcontainers (fluxo completo `MockMvc` → controller → use case → JPA → Postgres) por feature.

## Coleção Postman

Uma coleção Postman em formato JSON está incluída no repositório em
[clean-resmapi.postman_collection.json](clean-resmapi.postman_collection.json), cobrindo:

Alternativamente, para acessar a coleção diretamente em Postman, clique
[aqui](https://vitorbetmann-5356326.postman.co/workspace/Vitor-Betmann's-Workspace~88c6f948-7512-4287-86ff-a0109b20c44c/collection/49832651-64cb016d-e939-48f0-a740-158eeef7715c?action=share&source=copy-link&creator=49832651)

- Cadeia completa de criação → leitura → atualização nos quatro recursos (`UserType` → `User` → `Restaurant` →
  `MenuItem`), com ids capturados automaticamente entre as requisições
- Cadastro válido e inválido (nome/e-mail duplicado, chaves estrangeiras inexistentes, um usuário que não é `Owner`
  tentando ser dono de um restaurante, preço de item de cardápio não positivo)
- Exclusão de todos os recursos, em ordem segura para as FKs, permitindo rodar a coleção do zero novamente

Cada requisição valida seu status code esperado, então rodar a coleção inteira também funciona como um smoke test
contra uma instância em execução.
