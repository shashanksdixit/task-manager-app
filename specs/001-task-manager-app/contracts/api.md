# API Contract: Task Manager

Base path: `/api/tasks`

Endpoints:

1. GET /api/tasks
- Description: List tasks with optional filtering
- Query parameters:
  - `keyword` (String, optional): case-insensitive search on title and description
  - `status` (String, optional): filter by `Status` enum value (`TODO`|`IN_PROGRESS`|`COMPLETE`)
  - `priority` (String, optional): filter by `Priority` enum value (`LOW`|`MEDIUM`|`HIGH`)
  - Note: all parameters are optional and combinable (NFR-6)
- Response: 200 OK
- Body: JSON array of `TaskDto`
- Example requests:
  - `GET /api/tasks` (returns all tasks)
  - `GET /api/tasks?keyword=fix`
  - `GET /api/tasks?status=TODO`
  - `GET /api/tasks?priority=HIGH`
  - `GET /api/tasks?keyword=fix&status=TODO&priority=HIGH`
- Example response:

  [
    {
      "id": 3,
      "title": "Fix login bug",
      "description": "Resolve the bug preventing users from signing in",
      "priority": "HIGH",
      "status": "TODO",
      "dueDate": "2026-06-10",
      "createdAt": "2026-05-28T09:00:00Z",
      "updatedAt": "2026-05-28T09:00:00Z"
    }
  ]


2. GET /api/tasks/{id}
- Description: Get task by id
- Response: 200 OK or 404 Not Found
- Body: `TaskDto`

3. POST /api/tasks
- Description: Create a task
- Request: `CreateTaskRequest` JSON
  - `title` (string, required)
  - `description` (string)
  - `priority` (LOW|MEDIUM|HIGH)
  - `dueDate` (YYYY-MM-DD)
- Response: 201 Created
- Body: `TaskDto`

4. PUT /api/tasks/{id}
- Description: Update an existing task
- Request: `UpdateTaskRequest` JSON
- Response: 200 OK or 404 Not Found
- Body: `TaskDto`

5. DELETE /api/tasks/{id}
- Description: Delete task
- Response: 204 No Content or 404 Not Found

6. POST /api/tasks/{id}/status
- Description: Change status of a task
- Request: JSON `{ "status": "IN_PROGRESS" }`
- Response: 200 OK
- Body: `TaskDto`

Error responses:
- 400 Bad Request: Validation error; response includes validation messages.
- 404 Not Found: Resource not found.
- 500 Internal Server Error: Unexpected failure.

Sample `TaskDto` JSON:

{
  "id": 1,
  "title": "Buy groceries",
  "description": "Milk, eggs, bread",
  "priority": "MEDIUM",
  "status": "TODO",
  "dueDate": "2026-06-01",
  "createdAt": "2026-05-27T12:34:56Z",
  "updatedAt": "2026-05-27T12:34:56Z"
}
