# API Contract: React Frontend Integration

The React frontend integrates with the Spring Boot backend via the following task API endpoints. All requests use `application/json`.

## Base URL

`http://localhost:8080/api/tasks`

## Endpoints

### GET /api/tasks

Query parameters:
- `keyword` (optional): search text for title or description
- `status` (optional): task status (`TODO`, `IN_PROGRESS`, `COMPLETE`)
- `priority` (optional): task priority (`LOW`, `MEDIUM`, `HIGH`)

Response: `200 OK`

```json
[
  {
    "id": 1,
    "title": "Buy groceries",
    "description": "Milk, bread, eggs",
    "priority": "HIGH",
    "status": "TODO",
    "dueDate": "2026-06-10",
    "createdAt": "2026-06-03T14:00:00",
    "updatedAt": "2026-06-03T14:00:00"
  }
]
```

### GET /api/tasks/{id}

Response: `200 OK`

```json
{
  "id": 1,
  "title": "Buy groceries",
  "description": "Milk, bread, eggs",
  "priority": "HIGH",
  "status": "TODO",
  "dueDate": "2026-06-10",
  "createdAt": "2026-06-03T14:00:00",
  "updatedAt": "2026-06-03T14:00:00"
}
```

### POST /api/tasks

Request body:

```json
{
  "title": "Buy groceries",
  "description": "Milk, bread, eggs",
  "priority": "HIGH",
  "dueDate": "2026-06-10"
}
```

Response: `201 Created`

```json
{
  "id": 2,
  "title": "Buy groceries",
  "description": "Milk, bread, eggs",
  "priority": "HIGH",
  "status": "TODO",
  "dueDate": "2026-06-10",
  "createdAt": "2026-06-03T14:05:00",
  "updatedAt": "2026-06-03T14:05:00"
}
```

### PUT /api/tasks/{id}

Request body:

```json
{
  "title": "Buy groceries and cook dinner",
  "description": "Milk, bread, eggs, veggies",
  "priority": "MEDIUM",
  "dueDate": "2026-06-11"
}
```

Response: `200 OK`

```json
{
  "id": 2,
  "title": "Buy groceries and cook dinner",
  "description": "Milk, bread, eggs, veggies",
  "priority": "MEDIUM",
  "status": "TODO",
  "dueDate": "2026-06-11",
  "createdAt": "2026-06-03T14:05:00",
  "updatedAt": "2026-06-03T14:10:00"
}
```

### PATCH /api/tasks/{id}/status

Request body:

```json
{
  "status": "IN_PROGRESS"
}
```

Response: `200 OK`

```json
{
  "id": 2,
  "title": "Buy groceries and cook dinner",
  "description": "Milk, bread, eggs, veggies",
  "priority": "MEDIUM",
  "status": "IN_PROGRESS",
  "dueDate": "2026-06-11",
  "createdAt": "2026-06-03T14:05:00",
  "updatedAt": "2026-06-03T14:15:00"
}
```

### DELETE /api/tasks/{id}

Response: `204 No Content`

## Error Handling

The frontend should surface API errors using a consistent error state. Example error conditions:
- validation failure from backend when request body is invalid
- `404 Not Found` if a task id does not exist
- network failure when backend is unavailable

## Notes

- The frontend will treat empty or omitted query parameters as no filter.
- The backend accepts `status` and `priority` values as upper-case enumerations.
- For development, Vite will proxy `/api` to `http://localhost:8080` so API calls can be issued from `http://localhost:5173` without CORS issues.
