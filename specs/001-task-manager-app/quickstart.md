# Quickstart — Task Manager App

## 1. Prerequisites

- Java 21 JDK
- Maven 3.x

## 2. Build

```bash
mvn clean package -f taskmanager/pom.xml
```

## 3. Run

```bash
mvn spring-boot:run -f taskmanager/pom.xml
```

## 4. Access

- Frontend UI: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:taskdb`
  - Username: `sa`
  - Password: (empty)
- REST API base: http://localhost:8080/api/tasks

## 5. Run Tests

```bash
mvn test -f taskmanager/pom.xml
```

## 6. Sample API Calls

These curl examples cover all 6 endpoints exposed by `TaskController`.

### List all tasks

```bash
curl http://localhost:8080/api/tasks
```

### Get a task by ID

```bash
curl http://localhost:8080/api/tasks/1
```

### Create a new task

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Write docs","description":"Write the quickstart docs","priority":"MEDIUM","status":"TODO","dueDate":"2026-06-05"}'
```

### Update a task

```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy groceries and snacks","description":"Buy groceries for the week","priority":"LOW","status":"TODO","dueDate":"2026-06-03"}'
```

### Change task status

```bash
curl -X PATCH http://localhost:8080/api/tasks/1/status \
  -H "Content-Type: application/json" \
  -d '{"status":"IN_PROGRESS"}'
```

### Delete a task

```bash
curl -X DELETE http://localhost:8080/api/tasks/1
```

## 7. Sample Data

On startup, the app preloads 3 sample tasks via `DataLoader`:

1. `Buy groceries` — Priority: `LOW`, Status: `TODO`
2. `Prepare sprint report` — Priority: `HIGH`, Status: `IN_PROGRESS`
3. `Fix login bug` — Priority: `HIGH`, Status: `TODO`
