---
description: "Auto-generated task list for Task Manager feature"
---

# Tasks: Task Manager (task-manager-app)

**Input**: Design documents from `/specs/001-task-manager-app/`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [ ] T001 Create Maven Spring Boot project skeleton at backend/pom.xml
- [ ] T002 [P] Add Spring Boot, Spring Data JPA, H2, MapStruct, and testing dependencies in backend/pom.xml
- [ ] T003 [P] Add `backend/src/main/resources/application-dev.properties` with H2 and datasource placeholders
- [ ] T004 [P] Create frontend static scaffold at `backend/src/main/resources/static/index.html` and `backend/src/main/resources/static/app.js`
- [ ] T005 [P] Add project README and `.gitignore` at backend/ and example `quickstart.md` placeholder at specs/001-task-manager-app/quickstart.md

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core backend and infra that MUST be complete before user stories

- [ ] T006 Setup H2 console and JPA configuration in `backend/src/main/resources/application-dev.properties`
- [ ] T007 [P] Implement JPA `Task` entity in `backend/src/main/java/com/example/taskmanager/model/Task.java`
- [ ] T008 [P] Create DTO records (`TaskDto`, `CreateTaskRequest`, `UpdateTaskRequest`, `TaskStatusUpdateDto`) in `backend/src/main/java/com/example/taskmanager/dto/`
- [ ] T009 [P] Implement `TaskRepository` in `backend/src/main/java/com/example/taskmanager/repository/TaskRepository.java`
- [ ] T010 [P] Implement MapStruct `TaskMapper` interface in `backend/src/main/java/com/example/taskmanager/mapper/TaskMapper.java`
- [ ] T011 Implement global error handling (`GlobalExceptionHandler`) in `backend/src/main/java/com/example/taskmanager/api/GlobalExceptionHandler.java`
- [ ] T012 [P] Configure logging and structured error responses in `backend/src/main/resources/logging.properties` / application config
- [ ] T013 Add `DataLoader` (`ApplicationRunner`) to seed 3 sample tasks at `backend/src/main/java/com/example/taskmanager/bootstrap/DataLoader.java`

**Checkpoint**: Foundation ready — user story implementation may begin

---

## Phase 3: User Story 1 - Create Task (Priority: P1) 🎯 MVP

**Goal**: Allow users to create a task with required fields and persist it

**Independent Test**: Create request returns 201 and subsequent GET /api/tasks includes created item

### Tests (TDD - write first)
- [ ] T014 [P] [US1] Unit test for `TaskService.create` in `backend/src/test/java/com/example/taskmanager/service/TaskServiceTest.java`
- [ ] T015 [P] [US1] Controller MockMvc test for POST `/api/tasks` in `backend/src/test/java/com/example/taskmanager/controller/TaskControllerTest.java`

### Implementation
- [ ] T016 [US1] Implement `TaskService.create` in `backend/src/main/java/com/example/taskmanager/service/TaskService.java`
- [ ] T017 [US1] Implement POST `/api/tasks` in `backend/src/main/java/com/example/taskmanager/controller/TaskController.java`
- [ ] T018 [US1] Add validation annotations to `CreateTaskRequest` DTO in `backend/src/main/java/com/example/taskmanager/dto/CreateTaskRequest.java`
- [ ] T019 [US1] Add integration test for create flow in `backend/src/test/java/com/example/taskmanager/integration/TaskIntegrationTest.java`

**Checkpoint**: US1 should be independently testable and demoable

---

## Phase 4: User Story 2 - View Tasks (Priority: P1)

**Goal**: Provide endpoint and UI to list all tasks

**Independent Test**: GET `/api/tasks` returns list including seeded and newly created tasks

### Tests
- [ ] T020 [P] [US2] Unit test for `TaskService.listAll` in `backend/src/test/java/com/example/taskmanager/service/TaskServiceListTest.java`
- [ ] T021 [P] [US2] Controller MockMvc test for GET `/api/tasks` in `backend/src/test/java/com/example/taskmanager/controller/TaskControllerListTest.java`

### Implementation
- [ ] T022 [US2] Implement `TaskService.listAll` in `backend/src/main/java/com/example/taskmanager/service/TaskService.java`
- [ ] T023 [US2] Implement GET `/api/tasks` in `backend/src/main/java/com/example/taskmanager/controller/TaskController.java`
- [ ] T024 [US2] Wire frontend list view in `backend/src/main/resources/static/app.js` and update `index.html`

---

## Phase 5: User Story 3 - Update Task (Priority: P2)

**Goal**: Allow editing task fields and persist changes

**Independent Test**: PUT `/api/tasks/{id}` updates persisted task and returns 200

### Tests
- [ ] T025 [P] [US3] Unit test for `TaskService.update` in `backend/src/test/java/com/example/taskmanager/service/TaskServiceUpdateTest.java`
- [ ] T026 [P] [US3] Controller MockMvc test for PUT `/api/tasks/{id}` in `backend/src/test/java/com/example/taskmanager/controller/TaskControllerUpdateTest.java`

### Implementation
- [ ] T027 [US3] Implement `TaskService.update` in `backend/src/main/java/com/example/taskmanager/service/TaskService.java`
- [ ] T028 [US3] Implement PUT `/api/tasks/{id}` in `backend/src/main/java/com/example/taskmanager/controller/TaskController.java`
- [ ] T029 [US3] Add integration test for update flow in `backend/src/test/java/com/example/taskmanager/integration/TaskUpdateIntegrationTest.java`

---

## Phase 6: User Story 4 - Change Status (Priority: P2)

**Goal**: Allow status updates to TODO / IN_PROGRESS / COMPLETE via `/api/tasks/{id}/status`

**Independent Test**: POST `/api/tasks/{id}/status` sets new status and returns the updated TaskDto

### Tests
- [ ] T030 [P] [US4] Unit test for `TaskService.changeStatus` in `backend/src/test/java/com/example/taskmanager/service/TaskServiceStatusTest.java`
- [ ] T031 [P] [US4] Controller MockMvc test for PATCH  `/api/tasks/{id}/status` in `backend/src/test/java/com/example/taskmanager/controller/TaskControllerStatusTest.java`

### Implementation
- [ ] T032 [US4] Implement `TaskService.changeStatus` in `backend/src/main/java/com/example/taskmanager/service/TaskService.java`
- [ ] T033 [US4] Implement PATCH  `/api/tasks/{id}/status` in `backend/src/main/java/com/example/taskmanager/controller/TaskController.java`
- [ ] T034 [US4] Add integration test for status-change flow in `backend/src/test/java/com/example/taskmanager/integration/TaskStatusIntegrationTest.java`

---

## Phase 7: User Story 5 - Delete Task (Priority: P2)

**Goal**: Allow deletion of tasks

**Independent Test**: DELETE `/api/tasks/{id}` removes the resource and subsequent GET returns 404

### Tests
- [ ] T035 [P] [US5] Unit test for `TaskService.delete` in `backend/src/test/java/com/example/taskmanager/service/TaskServiceDeleteTest.java`
- [ ] T036 [P] [US5] Controller MockMvc test for DELETE `/api/tasks/{id}` in `backend/src/test/java/com/example/taskmanager/controller/TaskControllerDeleteTest.java`

### Implementation
- [ ] T037 [US5] Implement `TaskService.delete` in `backend/src/main/java/com/example/taskmanager/service/TaskService.java`
- [ ] T038 [US5] Implement DELETE `/api/tasks/{id}` in `backend/src/main/java/com/example/taskmanager/controller/TaskController.java`
- [ ] T039 [US5] Add integration test for delete flow in `backend/src/test/java/com/example/taskmanager/integration/TaskDeleteIntegrationTest.java`

---

## Phase 8: Search & Filter (Amendment v1.1.0)

**Goal**: Enable keyword, status, and priority filtering on `GET /api/tasks` with optional, combinable query parameters.

**Independent Test**: `GET /api/tasks` returns filtered results when query parameters are provided and returns all tasks when no filters are supplied.

### Contracts & Docs
- [ ] T044 [US6] Update `specs/001-task-manager-app/contracts/api.md` with optional `keyword`, `status`, and `priority` query parameters for GET `/api/tasks`

### Tests (TDD - write first)
- [ ] T045 [P] [US6] Unit test for `TaskService.listAll` filtering behavior in `backend/src/test/java/com/example/taskmanager/service/TaskServiceFilterTest.java`
- [ ] T046 [P] [US6] Controller MockMvc test for GET `/api/tasks` with `keyword`, `status`, and `priority` filters in `backend/src/test/java/com/example/taskmanager/controller/TaskControllerFilterTest.java`
- [ ] T047 [P] [US6] Repository @DataJpaTest for JPA Specification queries in `backend/src/test/java/com/example/taskmanager/repository/TaskRepositorySpecificationTest.java`

### Implementation
- [ ] T048 [US6] Implement `TaskSpecification` in `backend/src/main/java/com/example/taskmanager/repository/TaskSpecification.java`
- [ ] T049 [US6] Update `TaskRepository` to extend `JpaSpecificationExecutor<Task>` in `backend/src/main/java/com/example/taskmanager/repository/TaskRepository.java`
- [ ] T050 [US6] Update `TaskService.listAll` signature and implementation in `backend/src/main/java/com/example/taskmanager/service/TaskService.java`
- [ ] T051 [US6] Update `TaskController.getAllTasks` in `backend/src/main/java/com/example/taskmanager/controller/TaskController.java` to accept optional `@RequestParam` filters
- [ ] T052 [US6] Add integration tests for combined search and filter combinations in `backend/src/test/java/com/example/taskmanager/integration/TaskSearchFilterIntegrationTest.java`
- [ ] T053 [US6] Update frontend search bar in `backend/src/main/resources/static/app.js` and `backend/src/main/resources/static/index.html` to pass optional query parameters to `GET /api/tasks`
- [ ] T054 [US6] Update `specs/001-task-manager-app/quickstart.md` with search and filter usage examples for GET `/api/tasks`

---

## Phase N: Polish & Cross-Cutting Concerns

- [ ] T055 [P] Update `specs/001-task-manager-app/quickstart.md` with build, run, and test instructions
- [ ] T056 [P] Ensure H2 console available at `/h2-console` in dev profile (`backend/src/main/resources/application-dev.properties`)
- [ ] T057 [P] Documentation: update `README.md` and `specs/001-task-manager-app/` docs with sample data and demo steps
- [ ] T058 [P] Run formatting (`mvn fmt` or IDE) and ensure tests pass

---

## Dependencies & Execution Order

- Phase 1 (Setup) must be done first. Many Phase 1 tasks marked `[P]` can execute in parallel.
- Phase 2 (Foundational) BLOCKS all user stories and must be complete before Phase 3+ begin.
- Each User Story phase contains tests-first tasks that should be written and confirmed failing before implementation tasks in that phase.

## Parallel Opportunities

- Tasks marked `[P]` can be executed in parallel by different developers (e.g., DTOs, repositories, MapStruct mapper, unit tests).
- Once Phase 2 completes, user stories `US1`..`US5` can be worked on in parallel.

## Suggested MVP

- MVP scope: **User Story 1 (Create Task)** plus foundational Phase 2. Deliver a working API endpoint `POST /api/tasks` and a minimal frontend to create and view tasks.

---

Generated by speckit.tasks from `implementation-plan.md` and `spec.md`.
