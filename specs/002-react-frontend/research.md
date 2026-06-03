# Research: React Frontend Implementation

## Decision: React 18 + Vite frontend for task management

**What was chosen**
- Use React 18 with functional components and hooks only.
- Build with Vite in a separate `frontend/` folder.
- Use pure CSS for styling, no third-party UI frameworks.
- Use the Fetch API for all REST calls.
- Use server-side filtering via `GET /api/tasks?keyword=&status=&priority=`.
- Serve production build from `taskmanager/src/main/resources/static/`.

**Rationale**
- React 18 is modern, widely supported, and fits the requested component architecture.
- Vite provides fast startup and hot module replacement, ideal for branch experimentation.
- Pure CSS keeps the feature lightweight and avoids external styling dependencies.
- Fetch API satisfies the prompt requirement and avoids introducing Axios or additional libraries.
- Server-side filtering centralizes query logic in the backend and preserves the existing API contract.
- Separate frontend root prevents backend artifact clutter and makes production deployment explicit.

## Alternatives considered

- **Create React App**: rejected because Vite is explicitly requested and offers faster development iteration.
- **Axios or other HTTP client**: rejected because the prompt specifies Fetch API for REST calls.
- **CSS framework (Bootstrap, Tailwind, Material UI)**: rejected because the prompt requires pure CSS styling.
- **Redux / Zustand**: rejected by the prompt's state management requirement; `useState`/`useEffect` is sufficient for the app.
- **Client-side filtering**: rejected because the prompt and backend API support server-side filtering with query parameters.
- **Embedding React in backend static files only**: rejected in favor of a separate frontend workspace for easier development and branch isolation.

## Key decisions

- `App.jsx` will own global state: `tasks`, `filters`, `editingTask`, `deletingTaskId`, `loading`, and `error`.
- `useEffect` in `App.jsx` will reload tasks whenever filters change.
- Child components will receive callbacks as props: `onEdit`, `onDelete`, `onStatusChange`, `onSubmit`, `onCancel`.
- `TaskForm.jsx` will be a controlled modal for create and edit.
- `ConfirmDialog.jsx` will be reusable for delete confirmation.
- `SearchBar.jsx` will debounce text input and provide status/priority dropdowns.
- `StatusSelector.jsx` will allow inline status updates without a separate form.

## Research conclusion

The React frontend will use a clean component hierarchy and minimal dependencies to match the user's prompt and the repository's architecture. This approach maximizes developer productivity in the short term and keeps the implementation ready for future quality improvements such as automated frontend tests.
