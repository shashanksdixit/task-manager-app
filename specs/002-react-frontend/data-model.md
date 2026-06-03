# Data Model: React Frontend

## Primary Entities

### Task

The frontend will model tasks using the same domain shape as the backend API.

Fields:
- `id` (number): Unique backend identifier.
- `title` (string): Required. Short task name.
- `description` (string | null): Optional detailed text.
- `priority` (`Low` | `Medium` | `High`): Required priority level.
- `status` (`TODO` | `IN_PROGRESS` | `COMPLETE`): Required workflow state.
- `dueDate` (string | null): Optional ISO date string, e.g. `2026-06-10`.
- `createdAt` (string): Backend-generated timestamp.
- `updatedAt` (string): Backend-generated timestamp.

### Filter State

The app will manage search and filter state in `App.jsx`:
- `keyword` (string): Search text for title and description.
- `status` (`TODO` | `IN_PROGRESS` | `COMPLETE` | `ALL`): Active status filter.
- `priority` (`Low` | `Medium` | `High` | `ALL`): Active priority filter.

### UI State

Additional state tracked in `App.jsx`:
- `tasks` (Task[]): loaded task list.
- `editingTask` (Task | null): task being edited.
- `deletingTaskId` (number | null): id pending deletion confirmation.
- `loading` (boolean): whether an API request is active.
- `error` (string | null): user-visible error message.

## Component State Relationships

- `App.jsx` is the source of truth for tasks and filters.
- `SearchBar.jsx` emits filter changes up to `App.jsx`.
- `TaskList.jsx` receives filtered tasks and renders `TaskCard` components.
- `TaskCard.jsx` exposes `onEdit`, `onDelete`, and `onStatusChange` callbacks.
- `TaskForm.jsx` renders a modal for create/edit and is controlled by `editingTask`.
- `ConfirmDialog.jsx` renders only when `deletingTaskId` is non-null.

## Validation Rules

Task creation and update must enforce:
- `title`: required, non-empty, maximum length 255.
- `description`: optional, maximum length 2000.
- `priority`: required, one of `Low`, `Medium`, `High`.
- `dueDate`: optional, valid ISO date string; null values are allowed.

UI behavior for invalid data:
- Show inline form validation for required fields.
- Prevent submit when title is empty or priority is not selected.
- Clear or normalize invalid `dueDate` input before sending requests.

## State Transitions

Task status may change directly between any valid states via inline selector.

Transitions:
- `TODO` → `IN_PROGRESS`
- `TODO` → `COMPLETE`
- `IN_PROGRESS` → `TODO`
- `IN_PROGRESS` → `COMPLETE`
- `COMPLETE` → `TODO` or `IN_PROGRESS`

The status selector permits direct selection of any valid state, matching the backend status update contract.

## Integration Model

The frontend will call the backend API through `frontend/src/api.js`.

Service functions:
- `fetchTasks({ keyword, status, priority })`
- `createTask(taskData)`
- `updateTask(id, taskData)`
- `deleteTask(id)`
- `changeStatus(id, status)`

All functions return promises and handle fetch errors consistently.

## Deployment Model

- Development: Vite dev server on `http://localhost:5173` with proxying `/api` to `http://localhost:8080`.
- Production: Vite build output copied into `taskmanager/src/main/resources/static/` so Spring Boot serves the static site.
