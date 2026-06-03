# Task Manager App

A simple task management application built with Java and Spring Boot. It provides a REST API for creating, reading, updating, and deleting tasks, with a lightweight vanilla JavaScript frontend.

## Tech Stack

- Java 21
- Spring Boot 3.x
- H2 Database
- MapStruct
- Vanilla JavaScript

## Architecture Overview

The application follows a standard Spring Boot layered architecture:

- Controller → handles HTTP requests and responses
- Service → contains business logic
- Repository → interfaces with the database
- H2 → in-memory database for persistence

## Quick Start

1. Build the project:
   ```bash
   ./mvnw clean package
   ```
2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Access the API:
   - `http://localhost:8080/api/tasks`
   - `http://localhost:8080/`

## REST API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/tasks | Create task |
| GET | /api/tasks | Get all tasks |
| GET | /api/tasks/{id} | Get task by id |
| PUT | /api/tasks/{id} | Update task |
| PATCH | /api/tasks/{id}/status | Change status |
| DELETE | /api/tasks/{id} | Delete task |

## Full Specification

For the full feature specification, see `specs/001-task-manager-app/`.

