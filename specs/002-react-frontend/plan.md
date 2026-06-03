# Implementation Plan: React Frontend

**Branch**: `002-react-frontend` | **Date**: 2026-06-03 | **Spec**: specs/002-react-frontend/spec.md

**Input**: React frontend implementation of the Task Manager application, using React 18, Vite, pure CSS, and the existing Spring Boot backend at `http://localhost:8080`.

## Summary

Implement a separate Vite-powered React frontend in `frontend/` that mirrors the existing Vanilla JS task manager UI and integrates with the Spring Boot backend via the Fetch API. The application will keep runtime state in `App.jsx`, use functional components and hooks only, and rely on server-side `GET /api/tasks` filtering for keyword, status, and priority.

The frontend will support task creation, editing, deletion with confirmation, inline status updates, search, and filters. Production builds will be copied to `taskmanager/src/main/resources/static/` for backend hosting. Development will use a Vite dev server on port `5173` with a `/api` proxy to the backend.

## Technical Context

**Language/Version**: JavaScript, React 18, ES2024

**Primary Dependencies**: React 18, Vite, pure CSS, Fetch API

**Storage**: browser runtime state only; backend persists tasks

**Testing**: manual acceptance testing in browser; no automated React tests in v1

**Target Platform**: modern desktop browsers; development on `localhost:5173`, production served from Spring Boot static resources

**Project Type**: frontend single-page application integrated with a Spring Boot backend

**Performance Goals**:
- Search/filter response visible within 500ms for task lists under 1000 items
- Vite startup and build under 10 seconds
- Minimal runtime state overhead for tasks and filters

**Constraints**:
- No external UI component libraries
- No external state management libraries
- Use only React functional components with hooks
- Build output must be copyable to backend static folder
- Use native Fetch API for all backend calls
- Use server-side filtering for search and filters

**Scale/Scope**:
- Single-page frontend for the Task Manager UI
- Supports task list CRUD, search, status updates, and filters
- Desktop-first UX, with mobile/responsiveness out of v1 scope

## Constitution Check

The plan aligns with the project constitution by keeping the UI architecture simple, documenting user workflows, and using explicit state ownership in `App.jsx`. It also preserves maintainability through a small, well-defined component tree and consistent API integration.

A planned deviation exists for Constitution Article III: this feature intentionally omits automated React tests in v1 to speed branch experimentation. This is justified by the current scope and will be mitigated through manual acceptance testing and the existing backend integration tests. Automated frontend tests should be added in a follow-up iteration.

## Project Structure

### Documentation (this feature)

```text
specs/002-react-frontend/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── api.md
└── spec.md
```

### Source Code (repository root)

```text
frontend/
├── package.json
├── vite.config.js
├── index.html
├── src/
│   ├── main.jsx
│   ├── App.jsx
│   ├── api.js
│   ├── index.css
│   └── components/
│       ├── TaskList.jsx
│       ├── TaskCard.jsx
│       ├── TaskForm.jsx
│       ├── SearchBar.jsx
│       ├── StatusSelector.jsx
│       └── ConfirmDialog.jsx
└── public/ (optional)

taskmanager/src/main/resources/static/
└── [production build output copied here]
```

**Structure Decision**: A standalone `frontend/` workspace isolates React implementation from the existing Spring Boot backend while enabling production deployment into the backend static resource folder.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| No automated React tests in v1 | Branch experimentation and faster validation | Automated frontend tests are deferred until the UI shape stabilizes |

## Phase 0 — Research & Decisions

- Confirmed React 18 + Vite as the frontend stack.
- Chose pure CSS to satisfy the requested styling constraint.
- Chose the Fetch API for all backend communication.
- Decided to use server-side filtering via `GET /api/tasks` query params.
- Decided to keep global UI state in `App.jsx` and pass callbacks to child components.
- Decided production output should be copied into `taskmanager/src/main/resources/static/`.

## Phase 1 — Design & Contracts

- Build the frontend scaffold in `frontend/`.
- Document the task data model and filter state in `data-model.md`.
- Create `frontend/src/api.js` with `fetchTasks`, `createTask`, `updateTask`, `deleteTask`, and `changeStatus`.
- Document the backend API contract in `contracts/api.md`.
- Capture dev and production workflows in `quickstart.md`.

## Phase 2 — Implementation Preparation

- After plan approval, generate `tasks.md` describing the detailed implementation steps.
- Implement core UI flows first: task list, create/edit modal, inline status change, and delete confirmation.
- Validate all acceptance criteria from `specs/002-react-frontend/spec.md` through manual testing.

## Phase 3 — Core Infrastructure
- Create Vite project scaffold with React 18
- Configure vite.config.js with /api proxy to localhost:8080
- Create api.js with all 5 API functions
- Create App.jsx with global state and useEffect for fetching
- Add error state in App.jsx — show error banner when API calls fail
- All api.js functions throw errors with meaningful messages
- Loading state shows spinner while fetching

## Phase 4 — Component Implementation (bottom-up)
Build in this order (leaf components first, root last):
1. ConfirmDialog.jsx — simplest, no dependencies
2. StatusSelector.jsx — single dropdown, no dependencies  
3. SearchBar.jsx — inputs and filter dropdowns
4. TaskForm.jsx — modal form for create/edit
5. TaskCard.jsx — uses StatusSelector and ConfirmDialog
6. TaskList.jsx — uses TaskCard, shows empty state
7. App.jsx — wires everything together

## Phase 5 — Styling & Polish
- index.css with CSS variables for theming
- Status badges (grey/blue/green)
- Priority badges (green/orange/red)
- Past due date warning ⚠️
- Modal overlay styles
- Responsive layout for desktop

## Phase 6 — Integration & Validation
- Run Vite dev server and test against live Spring Boot backend
- Validate all 5 User Stories manually
- Verify feature parity with Vanilla JS implementation
- Copy production build to Spring Boot static folder
- Update quickstart.md with React-specific instructions