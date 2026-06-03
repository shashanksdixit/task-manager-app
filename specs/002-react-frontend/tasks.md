# Tasks: React Frontend Implementation

**Input**: specs/002-react-frontend/plan.md, specs/002-react-frontend/spec.md, specs/002-react-frontend/contracts/api.md

## Phase 1: Setup (Shared Infrastructure)

- [ ] T001 Create `frontend/` React 18 project scaffold in `frontend/`
- [ ] T002 Create `frontend/src/main.jsx`, `frontend/src/App.jsx`, `frontend/src/api.js`, and `frontend/src/index.css`
- [ ] T003 [P] Configure `frontend/vite.config.js` with `/api` proxy to `http://localhost:8080`
- [ ] T004 [P] Add npm scripts for `dev`, `build`, `preview`, and production copy to `taskmanager/src/main/resources/static/` in `frontend/package.json`

---

## Phase 2: Foundational (Blocking Prerequisites)

- [ ] T005 Create `frontend/src/api.js` and export the Fetch API helper function used by all backend calls
- [ ] T006 [P] Implement `fetchTasks` in `frontend/src/api.js` with support for `keyword`, `status`, and `priority` query params
- [ ] T007 [P] Implement `createTask` in `frontend/src/api.js` calling `POST /api/tasks`
- [ ] T008 [P] Implement `updateTask` in `frontend/src/api.js` calling `PUT /api/tasks/{id}`
- [ ] T009 [P] Implement `deleteTask` in `frontend/src/api.js` calling `DELETE /api/tasks/{id}`
- [ ] T010 [P] Implement `changeTaskStatus` in `frontend/src/api.js` calling `PATCH /api/tasks/{id}/status`
- [ ] T011 [P] Create `frontend/src/components/ConfirmDialog.jsx` with confirm and cancel callbacks
- [ ] T012 [P] Create `frontend/src/components/StatusSelector.jsx` with TODO / IN_PROGRESS / COMPLETE options
- [ ] T013 [P] Create `frontend/src/components/SearchBar.jsx` with search input and priority/status filter controls
- [ ] T014 [P] Create `frontend/src/components/TaskForm.jsx` with title, description, priority, due date, save, and cancel behavior
- [ ] T015 [P] Create `frontend/src/components/TaskCard.jsx` to render task details and wire edit, status, and delete actions
- [ ] T016 [P] Create `frontend/src/components/TaskList.jsx` to render a list of `TaskCard` items and show an empty state
- [ ] T017 [P] Create `frontend/src/App.jsx` shell with task state, loading state, error state, and fetch effect
- [ ] T018 [P] Add `ConfirmDialog` styling to `frontend/src/index.css`
- [ ] T019 [P] Add `StatusSelector` styling to `frontend/src/index.css`
- [ ] T020 [P] Add `SearchBar` styling to `frontend/src/index.css`
- [ ] T021 [P] Add `TaskForm` styling to `frontend/src/index.css`
- [ ] T022 [P] Add `TaskCard` styling to `frontend/src/index.css`
- [ ] T023 [P] Add `TaskList` styling to `frontend/src/index.css`
- [ ] T024 [P] Add `App` layout and page styling to `frontend/src/index.css`

---

## Phase 3: User Story 1 - Create and Manage Tasks (Priority: P1)

**Goal**: Enable task creation with title, description, priority, due date and persist tasks to the backend.

**Independent Test**: Create a task, verify it appears in the list, refresh the page, and confirm it still exists.

- [ ] T025 [US1] Implement create task modal open/close flow in `frontend/src/App.jsx`
- [ ] T026 [US1] Wire `TaskForm` create mode to `createTask` in `frontend/src/App.jsx`
- [ ] T027 [US1] Implement save behavior and list refresh after task creation in `frontend/src/App.jsx`
- [ ] T028 [US1] Implement form validation for required title and priority in `frontend/src/components/TaskForm.jsx`
- [ ] T029 [US1] Implement persistence verification on page refresh via `fetchTasks` in `frontend/src/App.jsx`

---

## Phase 4: User Story 2 - View, Search, and Filter Tasks (Priority: P1)

**Goal**: Show tasks in a list and allow users to search keywords and filter by status and priority.

**Independent Test**: Create multiple tasks, apply search and filters, and verify only matching tasks are displayed.

- [ ] T030 [US2] Implement keyword search state and callback in `frontend/src/App.jsx`
- [ ] T031 [US2] Implement filter state for priority and status in `frontend/src/App.jsx`
- [ ] T032 [US2] Wire `SearchBar` controls to `fetchTasks` query parameters in `frontend/src/App.jsx`
- [ ] T033 [US2] Implement search/filter input rendering in `frontend/src/components/SearchBar.jsx`
- [ ] T034 [US2] Implement clear filters and reset behavior in `frontend/src/components/SearchBar.jsx`

---

## Phase 5: User Story 3 & 4 - Update Task Details and Manage Status (Priority: P2)

**Goal**: Allow editing task details and changing task status from TODO → IN_PROGRESS → COMPLETE.

**Independent Test**: Edit an existing task, update its status, refresh, and confirm the changes persist.

- [ ] T035 [US3] Implement edit task flow in `frontend/src/App.jsx` and `frontend/src/components/TaskForm.jsx`
- [ ] T036 [US3] Wire task selection to `TaskForm` edit mode in `frontend/src/components/TaskCard.jsx`
- [ ] T037 [US3] Implement update request behavior using `updateTask` in `frontend/src/App.jsx`
- [ ] T038 [US4] Render `StatusSelector` inside `frontend/src/components/TaskCard.jsx` for inline status changes
- [ ] T039 [US4] Wire `StatusSelector` selection to `changeTaskStatus` in `frontend/src/App.jsx`
- [ ] T040 [US4] Refresh the task list after status updates in `frontend/src/App.jsx`

---

## Phase 6: User Story 5 - Delete Tasks with Confirmation, Polish & Acceptance Testing (Priority: P3)

**Goal**: Provide safe task deletion with confirmation and polish the React frontend for production.

**Independent Test**: Delete a task via confirmation dialog, refresh, and confirm the task is removed.

- [ ] T041 [US5] Implement delete button in `frontend/src/components/TaskCard.jsx`
- [ ] T042 [US5] Implement confirmation dialog integration in `frontend/src/App.jsx` and `frontend/src/components/ConfirmDialog.jsx`
- [ ] T043 [US5] Implement delete task behavior using `deleteTask` in `frontend/src/App.jsx`
- [ ] T044 [US5] Implement cancel delete behavior in `frontend/src/components/ConfirmDialog.jsx`
- [ ] T045 [US5] Refresh the task list after deletion and verify persistence in `frontend/src/App.jsx`
- [ ] T046 [P] Implement loading spinner and backend error banner in `frontend/src/App.jsx`
- [ ] T047 [P] Update `specs/002-react-frontend/quickstart.md` with React dev and production validation steps
- [ ] T048 [P] Polish desktop layout and spacing in `frontend/src/index.css`

---

## Manual Acceptance Tests

- [ ] T049 [US1] Manually verify task creation, visibility, and persistence in the browser for `frontend/`
- [ ] T049b [P] Add past due date warning ⚠️ in TaskCard.jsx 
  when dueDate exists and is before today
- [ ] T049c [P] Add npm script and instructions to copy 
  frontend/dist/ to taskmanager/src/main/resources/static/
- [ ] T050 [US2] Manually verify keyword search, status filtering, and priority filtering in the browser for `frontend/`
- [ ] T051 [US3] Manually verify editing a task updates title, description, priority, and due date in `frontend/`
- [ ] T052 [US4] Manually verify inline status changes from TODO → IN_PROGRESS → COMPLETE persist after refresh in `frontend/`
- [ ] T053 [US5] Manually verify delete confirmation removes a task and the removal persists after refresh in `frontend/`

---

## Dependencies & Execution Order

- Phase 1: Setup must complete before Phase 2
- Phase 2: Foundational must complete before Phase 3, Phase 4, Phase 5, and Phase 6
- Phase 3: US1 can be delivered independently after foundational work
- Phase 4: US2 can be delivered independently after foundational work
- Phase 5: US3 and US4 can be delivered independently after foundational work
- Phase 6: US5 and polish tasks follow the task CRUD and status flows

### User Story Completion Order

1. US1 - Create and Manage Tasks
2. US2 - View, Search, and Filter Tasks
3. US3 - Update Task Details
4. US4 - Manage Task Status
5. US5 - Delete Tasks with Confirmation

### Parallel Execution Examples

- Phase 1 and Phase 2 `frontend/src/api.js` function tasks can be worked in parallel because they target independent concerns
- Component creation tasks in Phase 2 (`ConfirmDialog.jsx`, `StatusSelector.jsx`, `SearchBar.jsx`, `TaskForm.jsx`, `TaskCard.jsx`, `TaskList.jsx`, `App.jsx`) can be worked in parallel by different developers
- CSS styling tasks for each component can be worked in parallel in `frontend/src/index.css`
- Story implementation work for US3 and US4 can be separated after foundational components exist

## Implementation Strategy

- MVP first: complete Phase 1, Phase 2, then Phase 3 (US1)
- Validate US1 independently before investing in search/filter or edit/status workflows
- Add US2, US3, US4, and US5 in priority order while preserving independent testability
- Finish with polish tasks and quickstart validation to ensure the React frontend is production-ready
