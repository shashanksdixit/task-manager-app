# Feature Specification: React Frontend Implementation

**Feature Branch**: `002-react-frontend`

**Created**: 2026-06-03

**Status**: Draft

**Input**: React frontend implementation of the Task Manager application using React 18, Vite, and pure CSS, providing identical functionality to the existing Vanilla JS frontend while reusing the Spring Boot REST API backend.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Create and Manage Tasks (Priority: P1)

Users need to create tasks with structured information (title, description, priority, due date) and manage them throughout their lifecycle. This is the core value proposition of the Task Manager.

**Why this priority**: P1 - Creating and viewing tasks is the fundamental functionality that all users need. Without this, the application has no value.

**Independent Test**: Can be fully tested by launching the app, creating a task, verifying it appears in the list, and confirming persistence after page refresh. Delivers immediate, standalone value.

**Acceptance Scenarios**:

1. **Given** the Task Manager app is loaded, **When** the user clicks "Create Task", **Then** a form appears with fields for title, description, priority (dropdown), and due date (date picker)
2. **Given** the task creation form is displayed, **When** the user enters "Buy groceries" as title and "Milk, bread, eggs" as description and selects "High" priority and sets due date to tomorrow, **Then** clicking "Save" creates the task and displays it in the task list
3. **Given** a task has been created, **When** the user refreshes the page, **Then** the task persists and is still visible in the list
4. **Given** the task creation form is open, **When** the user clicks "Cancel", **Then** the form closes without creating a task

---

### User Story 2 - View, Search, and Filter Tasks (Priority: P1)

Users need to view all tasks and quickly find specific tasks using search and filtering capabilities (by status, priority, and keywords).

**Why this priority**: P1 - Finding tasks is as critical as creating them. Users with many tasks need efficient ways to locate relevant items.

**Independent Test**: Can be tested by creating multiple tasks with different statuses and priorities, then verifying search/filter functionality independently returns correct subsets of tasks.

**Acceptance Scenarios**:

1. **Given** multiple tasks exist with different statuses and priorities, **When** the user loads the app, **Then** all tasks are displayed in the task list
2. **Given** tasks are displayed, **When** the user enters "buy" in the search field, **Then** only tasks with "buy" in title or description are shown (case-insensitive)
3. **Given** tasks are displayed, **When** the user selects "High" from the priority filter, **Then** only high-priority tasks are shown
4. **Given** tasks are displayed, **When** the user selects "IN_PROGRESS" from the status filter, **Then** only in-progress tasks are shown
5. **Given** filter/search is active, **When** the user clears the search box or resets filters, **Then** all tasks are displayed again
6. **Given** the search and multiple filters are active, **When** tasks are displayed, **Then** all conditions are applied (AND logic)

---

### User Story 3 - Update Task Details (Priority: P2)

Users need to modify existing task information (title, description, priority, due date) when circumstances change.

**Why this priority**: P2 - Updating tasks is important but less urgent than creation and viewing. Many task workflows don't require frequent updates.

**Independent Test**: Can be tested by selecting a task, editing a field, saving, and verifying the change persists in both the list and after page refresh.

**Acceptance Scenarios**:

1. **Given** a task is displayed in the list, **When** the user clicks on the task or an edit button, **Then** the task details are shown in an editable form
2. **Given** the task edit form is open, **When** the user changes the title from "Buy groceries" to "Buy groceries and cook dinner", **Then** clicking "Save" updates the task
3. **Given** a task has been updated, **When** the user refreshes the page, **Then** the changes persist
4. **Given** the task edit form is open, **When** the user clicks "Cancel", **Then** changes are discarded and the form closes

---

### User Story 4 - Manage Task Status (Priority: P2)

Users need to track task progress by changing task status through TODO → IN_PROGRESS → COMPLETE workflow.

**Why this priority**: P2 - Status management is essential for workflow tracking but can work with a separate modal or inline controls, so not blocking initial MVP.

**Independent Test**: Can be tested by creating a task, changing its status through the workflow, and verifying status changes persist and appear correctly in filtered views.

**Acceptance Scenarios**:

1. **Given** a task with status "TODO" is displayed, **When** the user clicks to change status, **Then** a status selector appears with options: TODO, IN_PROGRESS, COMPLETE
2. **Given** the status selector is open, **When** the user selects "IN_PROGRESS", **Then** the task status updates immediately and is reflected in the list
3. **Given** a task is in "IN_PROGRESS" status, **When** the user changes it to "COMPLETE", **Then** the status updates and the task may visually appear as completed (e.g., strikethrough)
4. **Given** a task status has been changed, **When** the user refreshes the page, **Then** the new status persists

---

### User Story 5 - Delete Tasks with Confirmation (Priority: P3)

Users need the ability to remove tasks with safeguards against accidental deletion.

**Why this priority**: P3 - Deletion is important for keeping the task list clean but is less frequent than creation/viewing. A confirmation dialog prevents accidents.

**Independent Test**: Can be tested by attempting to delete a task, confirming the deletion, and verifying it's removed from the list and after page refresh.

**Acceptance Scenarios**:

1. **Given** a task is displayed, **When** the user clicks a delete button, **Then** a confirmation dialog appears asking "Are you sure you want to delete this task?"
2. **Given** the confirmation dialog is displayed, **When** the user clicks "Confirm" or "Delete", **Then** the task is removed from the list
3. **Given** the confirmation dialog is displayed, **When** the user clicks "Cancel", **Then** the dialog closes and the task remains
4. **Given** a task has been deleted, **When** the user refreshes the page, **Then** the task remains deleted (deletion persists)

---

### Edge Cases

- What happens when a user creates a task with only a title (no description, priority, or due date)? → Should allow creation with sensible defaults (description: empty, priority: "Medium" or unset, due date: optional)
- How does the system handle very long task titles or descriptions? → Should display with text truncation in list view, full text in detail/edit view
- What happens when a user searches for special characters or very specific keywords? → Search should handle special characters gracefully and return exact/partial matches
- How are tasks ordered in the list? → By creation date (newest first) or configurable sort order (assume newest first as default)
- What happens when the network connection is lost while creating a task? → Form should indicate network error and allow retry

## Requirements *(mandatory)*

### Functional Requirements

- **FR-1**: Frontend MUST provide a form to create tasks with fields: title (required), description (optional), priority (required, options: Low/Medium/High), and due date (optional date picker)
- **FR-2**: Frontend MUST display all tasks in a sortable/scrollable list view showing at minimum: task title, status, priority, and due date
- **FR-3**: Frontend MUST provide a keyword search feature that searches task titles and descriptions (case-insensitive partial match)
- **FR-4**: Frontend MUST provide filter controls for status (TODO, IN_PROGRESS, COMPLETE) and priority (Low, Medium, High) with multi-select or independent filtering
- **FR-5**: Frontend MUST allow users to edit task details (title, description, priority, due date) through an edit form or inline editing
- **FR-6**: Frontend MUST allow users to change task status through a status selector (TODO → IN_PROGRESS → COMPLETE) or dropdown
- **FR-7**: Frontend MUST provide a delete button for each task that triggers a confirmation dialog before deletion
- **FR-8**: Frontend MUST communicate with the Spring Boot REST API at `http://localhost:8080` using the Fetch API for all CRUD operations. PATCH /api/tasks/{id}/status — change task status
- **FR-9**: Frontend MUST implement all components using React 18 functional components with hooks only (useState, useEffect, useContext as needed; no class components)
- **FR-10**: Frontend MUST build using Vite as the build tool with optimized production output
- **FR-11**: Frontend MUST use only pure CSS (no external UI component libraries like Material-UI, Chakra UI, Bootstrap, etc.)
- **FR-12**: Frontend MUST implement the following component structure: App (root), TaskList, TaskCard, TaskForm, SearchBar, StatusSelector, ConfirmDialog
- **FR-13**: Frontend MUST manage application state using only React hooks (useState, useEffect) with no external state management library (no Redux, Zustand, etc.)
- **FR-14**: Frontend build output MUST be servable from the Spring Boot static folder (`src/main/resources/static/`) or configured as a proxy during development

### Key Entities

- **Task**: Represents a user task with properties: id (unique identifier from backend), title (string), description (string), priority (Low/Medium/High), status (TODO/IN_PROGRESS/COMPLETE), dueDate (optional date/timestamp)
- **Search Query**: Keyword string used for filtering tasks by title and description
- **Filter State**: Current active filters including priority selection(s) and status selection(s)

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-1**: React frontend provides 100% feature parity with existing Vanilla JS frontend for all documented task operations
- **SC-2**: Users can create a complete task (title, description, priority, due date) in under 30 seconds
- **SC-3**: Frontend displays search results within 500ms of user query input (for typical task lists with <1000 tasks)
- **SC-4**: React frontend builds successfully with Vite in under 10 seconds
- **SC-5**: All API calls to Spring Boot backend complete successfully and with appropriate error handling
- **SC-6**: React application renders without console errors or warnings related to missing dependencies, unhandled states, or component lifecycle issues
- **SC-7**: Task creation, updates, deletions, and status changes persist correctly in the backend (verified after page refresh)
- **SC-8**: Filtering and search functionality work independently and in combination without performance degradation

## Assumptions

- Users have modern browsers with ES6+ support (Chrome, Firefox, Safari, Edge latest versions)
- The existing Spring Boot REST API at `http://localhost:8080` is running and functional with the documented task endpoints (GET /api/tasks, POST /api/tasks, PUT /api/tasks/{id}, DELETE /api/tasks/{id}, etc.)
- Task priority values are Limited to three levels: Low, Medium, High (no custom priorities in v1)
- Task status follows a linear progression: TODO → IN_PROGRESS → COMPLETE (users can set any status directly; no workflow lock-in)
- Due dates are optional; tasks without due dates should display gracefully (empty state, "No due date" label, or similar)
- Frontend development serves the app locally during development (e.g., `http://localhost:5173` with Vite) and communicates with backend at `http://localhost:8080`
- The React frontend is a branch experimentation (feature branch); the existing Vanilla JS implementation remains the primary implementation until decision is made to merge or retire it
- Search and filtering uses server-side API query parameters (GET /api/tasks?keyword=fix&status=TODO&priority=HIGH) 
  already implemented in backend v1.1.0. No client-side filtering needed.
- Responsive design is out of scope for v1 (desktop-first implementation); mobile responsiveness can be added in v2
- Accessibility (WCAG compliance) is out of scope for v1; can be added in v2


## Amendment Log
| Version | Date | Change | Reason |
|---|---|---|---|
| 1.0.0 | 2026-06-03 | Initial React frontend spec | Branch experimentation |
