# AGENTS.md

## Repository Structure

Two codebases live under `taskmanager/`:
- **Backend**: Java 21 + Spring Boot 3.5 + H2 (in-memory) at `taskmanager/src/`
- **Frontend**: React 18 + Vite at `taskmanager/frontend/`

The root `taskmanager/` directory contains the Maven wrapper and `pom.xml`. There is no root-level build config.

## Backend Commands (run from `taskmanager/`)

```bash
cd taskmanager
./mvnw clean package        # build + tests
./mvnw spring-boot:run       # run on port 8080
./mvnw test                  # tests only
```

Run a single test class:
```bash
./mvnw test -Dtest=TaskIntegrationTest
```

Run a single test method:
```bash
./mvnw test -Dtest=TaskIntegrationTest#createTask_ShouldPersistAndReturnTask
```

## Frontend Commands (run from `taskmanager/frontend/`)

```bash
cd taskmanager/frontend
npm install                  # install deps
npm run dev                  # Vite dev server on :5173 (proxies /api to :8080)
npm run build                # production build
npm run copy-to-backend      # copies dist/ to backend static resources
npm run lint                 # ESLint
```

The Vite dev server proxies `/api` requests to `http://localhost:8080`. Backend must be running separately for the frontend to work.

## Build → Deploy Order

1. Frontend: `npm run build && npm run copy-to-backend` (from `taskmanager/frontend/`)
2. Backend: `./mvnw spring-boot:run` (from `taskmanager/`)

The `copy-to-backend` script copies `dist/` into `taskmanager/src/main/resources/static/` so Spring Boot serves it. After copying, rebuild the backend jar or restart the server.

## Key Gotchas

- **H2 in-memory database**: Data resets on every restart. `DataLoader` seeds sample tasks at startup.
- **MapStruct annotation processor**: Generates mapper implementations at compile time. If you modify `TaskMapper.java` interface methods, you must rebuild (`mvnw clean compile`) before the changes take effect.
- **Lombok**: Model and DTO classes use `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`. Don't add manual getters/setters.
- **DDL mode**: `spring.jpa.hibernate.ddl-auto=create-drop` — schema is created from JPA entities on startup and dropped on shutdown. To change the schema, edit the JPA entity (`Task.java`), not SQL scripts.
- **CORS**: `@CrossOrigin(origins = "*")` is set on the controller for development. This is intentional.
- **Enums**: `Status` (TODO, IN_PROGRESS, COMPLETE) and `Priority` (LOW, MEDIUM, HIGH) are stored as strings in the database.

## Testing

- **Backend only**: No automated frontend tests exist yet.
- **Test framework**: JUnit 5 + Mockito + Spring Boot Test + MockMvc.
- **Test locations**: `src/test/java/com/example/taskmanager/` — unit tests in `service/` and `controller/`, integration tests in `integration/`.
- **Integration tests** use `@SpringBootTest` with `@Transactional` (rolls back after each test).
- **No external services required**: H2 runs in-memory, no Docker needed.

## Code Conventions

- **Package structure**: `com.example.taskmanager.{controller,service,repository,model,dto,mapper,exception,config,bootstrap}`
- **DTOs**: Record types (`TaskDto`, `CreateTaskRequest`, `UpdateTaskRequest`, `TaskStatusUpdateDto`)
- **Mapper**: Single MapStruct interface (`TaskMapper`) for all conversions
- **Service layer**: Transactional, throws `EntityNotFoundException` for missing entities
- **API prefix**: All endpoints under `/api/tasks`
- **Frontend**: Functional components with hooks only, state managed in `App.jsx`, no external state libraries

## Spec-kit

Feature specifications live in `specs/`. The current active feature is `specs/002-react-frontend/`. Refer to `specs/002-react-frontend/plan.md` for the implementation plan and architecture decisions.
