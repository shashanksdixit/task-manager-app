---
name: taskmanager-review
description: Use when reviewing code changes in this Spring Boot + React task manager project. Checks against project-specific conventions.
---

# Task Manager Review Checklist

When reviewing code in this project, always check:
1. Lombok usage — never hand-write getters/setters on model/DTO classes
2. MapStruct — any TaskMapper interface change requires `mvnw clean compile` before it takes effect
3. Transactional boundaries — write operations must be @Transactional; prefer readOnly=true on pure queries
4. DTOs must be Java records, never classes
5. Controllers must stay thin — no business logic, only HTTP translation to service calls
