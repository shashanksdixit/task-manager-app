# Quickstart — Task Manager App

Requirements:
- Java 21 JDK (https://jdk.java.net/21/)
- Maven 3.8+

Build and run (development):

```bash
# from project root
mvn -U -f pom.xml clean package
# run the Spring Boot app
java -jar target/*.jar
```

Or use the Spring Boot plugin for faster dev:

```bash
mvn -U spring-boot:run
```

Notes:
- H2 console (dev profile) available at `/h2-console`.
- The application will preload 3 sample tasks using the `DataLoader` on startup.

Run tests:

```bash
mvn test
```

Frontend:
- A minimal `index.html` and `app.js` are served from `src/main/resources/static`.
- The frontend uses the Fetch API to call `/api/tasks` endpoints.

Development tips:
- Use `application.properties` to enable H2 console and show SQL:
  - `spring.h2.console.enabled=true`
  - `spring.jpa.show-sql=true`

Troubleshooting:
- Ensure JDK 21 is active on PATH and `JAVA_HOME`.
- If using older Maven, consider upgrading for compatibility with Spring Boot 3.x.
