# Task Manager (Full-Stack)

Short name: task-manager-app

## Summary

Provide users with a simple Task Manager that allows creating, viewing, updating, changing status, and deleting tasks. Tasks include a title, description, priority (LOW / MEDIUM / HIGH) and due date. The frontend will interact with a backend REST API to persist and retrieve tasks.

## Background

Users need a lightweight interface to track short-lived tasks and their statuses. The feature should be easy to use and support standard task lifecycle operations.

## Actors

- Primary: End user (any authenticated or anonymous person using the app)

## User Goals

- Create a new task with essential details.
- See a list of all tasks and their statuses at a glance.
- Edit task details to correct or extend information.
- Change a task's status to TODO, IN_PROGRESS, or COMPLETE.
- Delete tasks when they are no longer needed.

## Key Data

- Task
  - id: unique identifier
  - title: string (required)
  - description: string (optional)
  - priority: enum {LOW, MEDIUM, HIGH}
  - status: enum {TODO, IN_PROGRESS, COMPLETE}
  - dueDate: date (optional)
  - createdAt / updatedAt: timestamps

## Constraints & Validation

- `title` is required; max length 255 characters.
- `description` optional; max length 2000 characters.
- `priority` defaults to MEDIUM if not specified.
- `status` defaults to TODO on creation.
- `dueDate`, if provided, should be a valid date (past dates allowed but should present a warning in UI).

## User Scenarios & Testing

1. Create Task
   - Actor: End user
   - Steps: Open "New Task" form → enter `title` (required) → optionally enter `description`, `priority`, `dueDate` → submit
   - Acceptance: Task appears in task list with correct details and `status=TODO`.

2. View Tasks
   - Actor: End user
   - Steps: Open task list view
   - Acceptance: All saved tasks are listed, showing title, priority, status, and due date.

3. Update Task
   - Actor: End user
   - Steps: Select a task → open edit → modify fields → save
   - Acceptance: Changes persist and are reflected in list and detail view.

4. Change Status
   - Actor: End user
   - Steps: From task item or detail view, change status to IN_PROGRESS or COMPLETE
   - Acceptance: Status updates immediately in UI and persists.

5. Delete Task
   - Actor: End user
   - Steps: Trigger delete on task → confirm
   - Acceptance: Task is removed from list and no longer returned by API.

## Functional Requirements (Testable)

1. FR-1: Create Task
   - Given a user provides a valid `title`, when they submit the create form, then a new task is persisted and returned by the tasks list endpoint.

2. FR-2: Retrieve Tasks
   - Given tasks exist, when the user opens the task list, then the system returns all tasks with fields: id, title, description, priority, status, dueDate, createdAt, updatedAt.

3. FR-3: Update Task
   - Given a task exists, when the user sends an update with valid fields, then the stored task is updated accordingly.

4. FR-4: Change Status
   - Given a task exists, when the user sets `status` to TODO | IN_PROGRESS | COMPLETE, then the task's status is updated and persisted.

5. FR-5: Delete Task
   - Given a task exists, when the user requests deletion and confirms, then the task is removed and subsequent retrievals do not include it.

## Success Criteria (Measurable)

- SC-1: Core task flows (create, view, update, change status, delete) succeed in 95% of manual checks across 20 sample tasks.
- SC-2: A typical user can create and view a task in under 2 minutes (end-to-end form → list confirmation).
- SC-3: API endpoints return expected resources with correct fields for 100% of requests made by the frontend in functional tests.

## Key Entities

- `Task` (see Key Data section)

## Assumptions

- No user authentication is required for the initial scope unless requested later.
- Concurrency and multi-user conflicts are out of scope for initial delivery.
- Implementation preferences (requested tech stack) are recorded separately in `implementation-notes.md` inside the feature directory.
- REST API will follow standard HTTP conventions: POST to create, GET to retrieve, PUT to update, DELETE to remove.

## Dependencies

- None within the product scope; may depend on a chosen runtime and hosting environment.

## Acceptance Criteria

- All FR items pass automated or manual tests demonstrating persistence and retrieval.
- UI allows tasks to be created, edited, status-changed, and deleted with expected validations.

## Notes

- This specification focuses on WHAT and WHY. Implementation details are captured in `implementation-notes.md` to avoid leaking technology choices into the stakeholder-facing spec.

## Non-Functional Requirements

- NFR-1: API must respond within 500ms for all CRUD operations.
- NFR-2: Frontend must work on latest Chrome, Firefox, and Edge.
- NFR-3: Application must start with a pre-loaded set of 3 sample tasks for demo purposes.
- NFR-4: H2 console must be accessible at /h2-console for development inspection.


