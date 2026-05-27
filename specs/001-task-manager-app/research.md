# Research: Tech Decisions for Task Manager App

Decision: Use Spring Boot 3.x with Java 21
- Rationale: Spring Boot 3.x is the current major release with Jakarta EE alignment; Java 21 provides `record` types and modern language features suitable for concise DTOs.
- Alternatives considered: Quarkus/ Micronaut for smaller footprint; chosen: Spring Boot for familiarity and ecosystem.

Decision: Use MapStruct for DTO mapping
- Rationale: MapStruct generates compile-time mapping code, integrates well with records via recent MapStruct versions, provides good performance and traceability.
- Alternatives: ModelMapper (runtime reflection) — less performant and noisier mapping errors.

Decision: H2 in-memory database with Spring Data JPA
- Rationale: H2 supports fast in-memory development and an optional persistent file mode; Spring Data JPA reduces boilerplate for repositories.
- Alternatives: Derby, SQLite — H2 is most common for Spring dev and offers console.

Decision: Maven build, JUnit 5 + Mockito for testing
- Rationale: Maven is widely used; JUnit 5 is standard; Mockito simplifies mocking service/repository layers.

Decision: Vanilla JS frontend with Fetch API
- Rationale: Keeps frontend minimal and dependency-free for demo; easy to include as static resources in Spring Boot.
- Alternatives: Small SPA frameworks (React, Vue) — unnecessary for a demo app.

Decision: Pre-load 3 sample tasks with DataLoader
- Rationale: Satisfies NFR-3 (preloaded tasks) and provides immediate demo data. Implement via `ApplicationRunner`/`CommandLineRunner` that uses the `TaskService`.

All decisions captured above are implemented in the `implementation-plan.md` and will be reflected in generated artifacts.
