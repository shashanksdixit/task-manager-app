# Implementation Notes (developer-facing)

The user requested the following implementation preferences for this feature. These notes are intentionally separated from the stakeholder-facing spec.

- Backend: Spring Boot REST API using Java 21
- Database: H2 in-memory database for development
- Frontend: Vanilla JavaScript (no framework), communicating via REST API calls

Mapping to spec:
- The API should expose endpoints for create/read/update/delete tasks and for changing task status.
- The data model must include the fields described in the spec's Key Data section.
