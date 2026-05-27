# Implementation Plan: Task Manager App

Feature: Task Manager (from specs/001-task-manager-app/spec.md)

Tech stack (chosen):
- Spring Boot 3.x
- Java 21 (use `record` types for DTOs and simple immutable types)
- MapStruct for DTO mapping
- Spring Data JPA with H2 in-memory database
- Maven build
- JUnit 5 + Mockito for unit tests
- Vanilla JavaScript frontend using Fetch API
- Pre-load 3 sample tasks on startup using a `DataLoader` (ApplicationRunner)

Goals:
- Implement REST API for Task CRUD with DTOs and MapStruct mappings.
- Persist entities via Spring Data JPA to H2.
- Provide a tiny frontend that consumes the API.
- Include automated tests for controllers, services, and repositories.

## Phases

### Phase 0 — Research & Decisions
- Confirm tech choices and rationale in `research.md`.

### Phase 1 — Design & Contracts
- Create `data-model.md` describing `Task` entity, fields, validation rules.
- Create `contracts/api.md` listing all endpoints and schemas:
  - POST   /api/tasks
  - GET    /api/tasks
  - GET    /api/tasks/{id}
  - PUT    /api/tasks/{id}
  - PATCH  /api/tasks/{id}/status
  - DELETE /api/tasks/{id}
- Create Spring Boot project skeleton with Maven and required dependencies.
- Define global error handling strategy using `@ControllerAdvice`.

### Phase 2 — Tests First (Constitution Article III)
- Write JUnit 5 unit tests for service layer (TaskService) — must FAIL first.
- Write JUnit 5 unit tests for controller layer (TaskController) using MockMvc.
- Write repository slice tests using @DataJpaTest.
- Write integration test using @SpringBootTest for full CRUD flow.
- Get all tests reviewed and confirmed before writing implementation code.

### Phase 3 — Backend Implementation
- Implement `Task` JPA entity with jakarta.validation annotations.
- Implement `TaskDto` and `TaskStatusUpdateDto` as Java 21 records.
- Implement MapStruct mapper (TaskMapper interface).
- Implement TaskRepository extending JpaRepository.
- Implement TaskService with transactional CRUD and status update operations.
- Implement TaskController with all 6 endpoints under /api/tasks.
- Implement GlobalExceptionHandler using @ControllerAdvice for:
  - 404 when task not found
  - 400 for validation errors
  - 500 for unexpected errors
- Add DataLoader (ApplicationRunner) to seed 3 sample tasks at startup.
- Enable H2 console at /h2-console for dev profile.

### Phase 4 — Frontend Implementation
- Create `src/main/resources/static/index.html` with task list UI.
- Create `src/main/resources/static/app.js` using Fetch API for all REST calls.
- UI must support: create, view, edit, change status, delete with confirmation.
- Show warning in UI when due date is in the past (NFR from spec).

### Phase 5 — Validation & Quickstart
- Run all tests and confirm 100% pass.
- Verify all FR items (FR-1 through FR-5) manually.
- Create `quickstart.md` with build, run, and test instructions.

Artifacts (to generate):
- `research.md` — decisions and alternatives
- `data-model.md` — entities and DTO definitions
- `/contracts/api.md` — REST API contract (endpoints + sample payloads)
- `quickstart.md` — build/run/test instructions

Outputs will be placed in the feature folder `specs/001-task-manager-app/`.

Implementation notes:
- Use DTOs for requests/responses; entities should not be serialized directly.
- Use MapStruct to map between `Task` entity and `TaskDto` record.
- Validate input via `jakarta.validation` annotations on DTO fields.
- Keep controllers thin; business logic in service layer for easier testing.

Path: specs/001-task-manager-app/implementation-plan.md
