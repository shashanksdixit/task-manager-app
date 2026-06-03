# Quickstart: React Frontend

## Prerequisites

- Node.js 20+ installed
- npm or yarn available
- Spring Boot backend running on `http://localhost:8080`
- Project root: `d:/Shashank/Train/git-spec-kit/task-manager-app`

## Development

1. Open a terminal at the repo root.
2. Navigate to the frontend folder:
   ```bash
   cd frontend
   ```
3. Install dependencies:
   ```bash
   npm install
   ```
4. Start the Vite dev server:
   ```bash
   npm run dev
   ```
5. Open the app in the browser at:
   ```text
   http://localhost:5173
   ```

### Notes

- The Vite config will proxy `/api` requests to `http://localhost:8080`.
- The backend must be running before attempting create/update/delete operations.
- If the backend is not available, the frontend will show a network error message.

## Production Build

1. Build the frontend assets:
   ```bash
   cd frontend
   npm run build
   ```
2. Copy the build output into the Spring Boot static folder:
   ```bash
   xcopy /e /y /i dist ..\taskmanager\src\main\resources\static
   ```
3. Run the backend:
   ```bash
   cd taskmanager
   .\mvnw spring-boot:run
   ```
4. Open the app at:
   ```text
   http://localhost:8080
   ```

## Manual Validation

Exercise the following acceptance scenarios from `specs/002-react-frontend/spec.md`:

- Create a task with title, description, priority, and due date.
- Refresh the browser and verify the task persists.
- Search by keyword and verify task list filtering.
- Filter by status and priority independently and in combination.
- Edit an existing task and verify persistence.
- Change a task’s status using the inline status selector.
- Delete a task and confirm via the confirmation dialog.

## Notes

- v1 is intentionally limited to manual frontend testing. No automated React tests are included in this branch.
- Use `npm run build` before copying assets to the backend static folder to validate production output.
