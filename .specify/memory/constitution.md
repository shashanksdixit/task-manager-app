<!--
Sync Impact Report
- Version change: none → 1.0.0
- Modified principles: Added 5 core principles for task workflows, domain modeling, testing, maintainability, and versioning
- Added sections: Technology and Compliance; Development Workflow
- Removed sections: none
- Templates requiring updates: ✅ .specify/templates/plan-template.md, .specify/templates/spec-template.md, .specify/templates/tasks-template.md
- Follow-up TODOs: none
-->

# Task Manager App Constitution

## Core Principles

### I. User-Centric Task Workflows
Every capability MUST be defined by a real task outcome and validated by a user journey.
- Requirements are expressed as task workflows rather than internal implementation details.
- Acceptance is measured by successful task completion and clear user-facing results.

### II. Strong Domain Modeling
The project MUST represent tasks, users, priorities, and state transitions with explicit domain models.
- Business rules are encoded in the model, not hidden in UI or integration layers.
- Entities and relationships are documented, tested, and aligned with the app's task-management purpose.

### III. Test-Driven Delivery
All new functionality MUST start with tests that fail before implementation.
- Unit tests must cover individual domain rules and component behavior.
- Integration or contract tests must verify end-to-end task workflows.

### IV. Maintainable Architecture
Code MUST remain simple, modular, and easy to reason about.
- Components are organized by responsibility and dependencies are explicit.
- Common concerns such as error handling, logging, validation, and configuration are visible and standardized.

### V. Continuous Quality & Versioning
The project MUST use semantic versioning and require review for breaking changes.
- Public-facing changes follow MAJOR.MINOR.PATCH semantics.
- Incompatible changes are documented, justified, and communicated before merge.

## Technology and Compliance
The app is built as a Spring Boot service with Java, Maven, REST/JSON APIs, and automated tests.
- No secrets, credentials, or sensitive data may be committed to source control.
- Dependency upgrades must be reviewed for security and compatibility.
- Production-readiness requires automated builds, test execution, and environment-specific configuration isolation.

## Development Workflow
Feature work MUST follow a written spec, a validated plan, and a reviewed PR.
- Every change must include test coverage and a regression verification step before merge.
- Complexity must be justified in the PR description and aligned with these principles.
- Reviewers must verify compliance with the constitution, especially for domain, testing, and compatibility decisions.

## Governance
The constitution is the source of truth for project decisions.
- Amendments require a documented proposal, review by the repository owner or designated maintainer, and an update to affected plans/specs.
- Version bumps follow semantic versioning:
  - MAJOR for incompatible APIs or workflow changes,
  - MINOR for new capabilities or principle additions,
  - PATCH for clarifications, wording fixes, and non-behavioral refinements.
- Compliance review is required for every major or minor change.

## Tech Stack
Java 21, Spring Boot, H2, Maven

## Architecture
Layered. Controller -> Service -> Repository 
Do not return Java entity objects directly. Use data transfer object (dto). Use MapStruct for entity-to-DTO conversion to eliminate boilerplate mapping code.

## Testing
JUnit, test-first

## API Style
REST, JSON responses

**Version**: 1.0.0 | **Ratified**: 2026-05-27 | **Last Amended**: 2026-05-27

