# Adventure Backend

The backend is the data and logic layer of the adventure application. It is built with Java and Spring Boot and exposes a REST API for the frontend. The service is responsible for delivering adventure books, story sections, and decision paths to the client application.

## Overview

This service loads a collection of adventure stories from JSON files and stores them in an in-memory H2 database. At startup, the application scans the configured story folders, reads the files, and inserts the content into the database so the API can serve it dynamically.

The backend follows a clean layered architecture with clear responsibility boundaries:

- Controller: handles HTTP requests and responses
- Service: contains application logic and orchestration
- Repository: manages persistence through Spring Data JPA
- Model: defines the domain entities
- Exception: centralizes error handling
- Util: contains startup and data-loading logic

This structure makes the project easier to maintain, test, and explain during an interview.

## Technology stack

- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## Prerequisites

- Java 21+
- Maven or Maven wrapper

## Quick start

```bash
cd adventure-backend
./mvnw.cmd clean install
./mvnw.cmd spring-boot:run
```

Application URLs:
- API base: http://localhost:8080/api
- H2 console: http://localhost:8080/api/h2-console/

## Project structure

- src/main/java/com/pictet/adventure/
  - AdventureBackendApplication.java
  - config/
  - controller/
  - dto/
  - exception/
  - model/
  - repository/
  - service/
  - util/

Story data is loaded from:
- `files/books/`

## API

```text
GET /api/books
GET /api/books/{id}
GET /api/books/title/{title}
GET /api/books/{bookId}/sections/{sectionId}
POST /api/books
```

## Data loading

At startup, the app reads the adventure book JSON files and persists them in the H2 database. This approach makes the content easy to update without changing Java code, and it allows the application to behave like a real data-driven product.

## Troubleshooting

- If the app does not start, verify Java is installed: `java -version`
- If the port is busy, ensure nothing else is listening on 8080
- If books do not load, check the `files/books` directory for valid JSON files
- If the IDE shows red classes, refresh or reimport the Maven project and run:

```bash
./mvnw.cmd clean compile
```

- If the port is blocked, run:

```cmd
netstat -ano | findstr :8090
taskkill /PID <PID_NUMBER> /F
```

- If the port is 8080 instead of 8090, use the corresponding PID and port in the same command.

## Why this backend matters

This backend acts as the core of the product: it owns the story data, manages the persistence layer, and exposes APIs that the frontend consumes. In an interview, this is a strong example of how a backend can be designed to support a rich interactive experience while staying organized and scalable.