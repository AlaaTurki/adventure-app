# Adventure App

## 1. Project overview

Adventure App is a full-stack interactive story application built with Spring Boot and Angular. It lets users browse a library of adventure books, start a game, read the current section, choose options, and continue until the story ends or the player's health reaches zero.

The project follows a clean client-server pattern: the backend owns the business rules, validation, and game progression, while the frontend focuses on rendering and user interaction.

## 2. Architecture

The application is split into two main parts:

- Backend: Java 21 + Spring Boot REST API
- Frontend: Angular + TypeScript + HTML + CSS

```text
Angular frontend
  ├─ Book catalog
  ├─ Search and difficulty filters
  ├─ Story playback
  └─ Game interactions
        │ HTTP
        ▼
Spring Boot backend
  ├─ Book validation
  ├─ Story loading
  ├─ Game lifecycle
  ├─ Health and consequences
  └─ Saved game support
        │
        ▼
JSON adventure files
```

The backend is the source of truth for game progression, health changes, section transitions, and end-state decisions. Angular consumes the API and displays the resulting state rather than recalculating rules itself.

## 3. Prerequisites

- Java 21 or newer
- Maven Wrapper (included with project)
- Node.js 22 or newer
- npm
- Angular CLI (installed via `npm install` in the frontend project)

## 4. Backend setup

From the repository root:

```bash
cd adventure-backend
./mvnw clean install
./mvnw spring-boot:run
```

The backend runs on:

- http://localhost:8080

If needed, you can inspect the H2 console in a local development setup, but the core assessment uses the REST endpoints directly.

## 5. Frontend setup

From the repository root:

```bash
cd adventure-frontend
npm install
npm start
```

The frontend runs on:

- http://localhost:4200

## 6. How to run

1. Start the backend.
2. Start the frontend.
3. Open http://localhost:4200
4. Browse the available books.
5. Select a book to begin a game.
6. Choose a story option to progress through sections.

## 7. API endpoints

```text
GET    /books
GET    /books/{id}
GET    /books/title/{title}
GET    /books/{bookId}/sections/{sectionId}
POST   /books

POST   /games/start?bookId={bookId}
GET    /games/{gameId}
POST   /games/{gameId}/choices
POST   /games/{gameId}/save
GET    /games/saved
```

## 8. Book validation rules

Books are validated before being stored or used in gameplay. A valid book must include:

- exactly one `BEGIN` section
- at least one `END` section
- unique section IDs
- valid `gotoId` references for every option
- no null choices
- no null `gotoId` values
- no non-END section without options

Invalid books are rejected with an appropriate `400 BAD_REQUEST` response.

## 9. Game rules

- Each game starts with 10 health.
- A choice can apply a health consequence such as `GAIN_HEALTH`, `LOSE_HEALTH`, or a signed numeric health value.
- When health reaches 0, the game status becomes `DEAD`.
- When a choice leads to an `END` section, the game status becomes `WON`.
- Any invalid option index, selection after a completed game, or missing game/book is rejected.

## 10. Save functionality

Users can save the current game snapshot via:

```text
POST /games/{gameId}/save
```

Saved games can be listed with:

```text
GET /games/saved
```

The response includes saved-game metadata and the serialized game snapshot.

## 11. Project structure

```text
adventure-app/
├── README.md
├── postman_collection.json
├── adventure-backend/
│   ├── pom.xml
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── files/
│   │   └── books/
│   └── src/
└── adventure-frontend/
    ├── package.json
    ├── package-lock.json
    ├── angular.json
    └── src/
```

## 12. Testing

Backend validation and business rules are covered by JUnit tests. The project includes tests for:

- valid and invalid books
- game start state
- health changes and end-state transitions
- invalid option handling
- missing book/game scenarios

Run the backend test suite with:

```bash
cd adventure-backend
./mvnw test
```

## 13. Known limitations / assumptions

- The supplied JSON adventure files are the source of assessment data; empty files are preserved instead of inventing new content.
- The application uses straightforward JSON-driven book definitions rather than a microservice, Kafka, or external data store.
- The backend enforces the business rules, and the frontend displays the resulting game state.
- The project intentionally keeps the scope focused on the assessment requirements without adding unrelated infrastructure.

## Author

Alaa Turki

Senior Full-Stack Developer

- The app is designed for local demo and interview use, with an in-memory database to keep setup simple.
- If the IDE shows red Java classes, refresh or reimport the Maven project and run a compile command. The backend compiles successfully.

## Documentation

- Backend: [adventure-backend/README.md](adventure-backend/README.md)
- Frontend: [adventure-frontend/README.md](adventure-frontend/README.md)

## Summary

This repository demonstrates a realistic full-stack application: a Java backend exposes story data, a frontend renders interactive narrative flows, and a data-driven architecture makes content easy to extend. The project is suitable for local development, demo use, and technical interview discussion because it covers architecture, API design, persistence, debugging, and UI integration.
