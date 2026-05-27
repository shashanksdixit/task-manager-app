# Data Model

Entity: Task
- id: Long (generated)
- title: String (required, max 255)
- description: String (optional, max 2000)
- priority: Enum {LOW, MEDIUM, HIGH} (default MEDIUM)
- status: Enum {TODO, IN_PROGRESS, COMPLETE} (default TODO)
- dueDate: LocalDate (optional)
- createdAt: Instant
- updatedAt: Instant

JPA mapping notes:
- Use `@Entity` with `@Table(name = "tasks")`.
- Use `@Enumerated(EnumType.STRING)` for `priority` and `status`.
- Use `@CreationTimestamp` / `@UpdateTimestamp` (Hibernate) or populate via `@PrePersist`/`@PreUpdate`.

DTOs (use Java 21 records):
- `record TaskDto(Long id, String title, String description, Priority priority, Status status, LocalDate dueDate, Instant createdAt, Instant updatedAt)`
- `record CreateTaskRequest(String title, String description, Priority priority, LocalDate dueDate)`
- `record UpdateTaskRequest(String title, String description, Priority priority, Status status, LocalDate dueDate)`

Validation rules (apply `jakarta.validation` on DTOs):
- `title` -> `@NotBlank`, `@Size(max = 255)`
- `description` -> `@Size(max = 2000)`

Mapping:
- Use MapStruct mapper interface `TaskMapper` with mapping methods:
  - `Task toEntity(CreateTaskRequest dto)`
  - `Task toEntity(UpdateTaskRequest dto, @MappingTarget Task task)`
  - `TaskDto toDto(Task entity)`

Repository:
- `interface TaskRepository extends JpaRepository<Task, Long>`

Service:
- `TaskService` methods: create, listAll, getById, update, changeStatus, delete

