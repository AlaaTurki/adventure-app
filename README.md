# Adventure App — Full Stack Interactive Story

## Project Overview

Adventure App is a full‑stack interactive story application (Spring Boot backend + Angular frontend). Users browse a library of adventure books, start a game, read sections, choose options, and progress until the story ends or the player's health reaches zero.

The backend contains business rules, validation, game progression and persistence. The frontend renders UI and calls the REST API.

## Architecture

- Frontend: Angular + TypeScript (client)
- Backend: Java 21 + Spring Boot (server)
- Data: JSON adventure files and an embedded database for runtime/saved snapshots

Client ↔ HTTP ↔ Server ↔ JSON book data / persistence

## Features

- Book library with search and difficulty filters
- Start a game from a book (health = 10)
- Section navigation and choice selection
- Health consequences and game end states (WON / DEAD)
- Save current game snapshot to the backend
- Add new books via the backend API (validated on save)

## Prerequisites

- Java 21+
- Node.js 22+
- npm
- Maven is provided via the Maven Wrapper (no global Maven required)

## How to run

Backend (recommended):

Windows

```powershell
cd adventure-backend
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

macOS / Linux

```bash
cd adventure-backend
./mvnw clean test
./mvnw spring-boot:run
```

Frontend

```bash
cd adventure-frontend
npm install
npm start
```

Open the frontend at http://localhost:4200 and the backend at http://localhost:8080.

## API (summary)

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

## Save Progression (additional feature)

- Save current game snapshot: POST /games/{gameId}/save
- List saved snapshots: GET /games/saved

The frontend exposes a "Save Progress" control which calls the backend save endpoint to persist the serialized game state.

## Adding Books (additional feature)

- New books can be added via POST /books with the book JSON in the request body.
- Incoming books are validated using the same rules the runtime enforces; invalid files are rejected with 400 responses.

## Book validation rules

- Exactly one `BEGIN` section
- At least one `END` section
- Unique section IDs
- Every option's `gotoId` must reference an existing section
- Non-END sections must have at least one option

## Testing

Run backend tests:

```bash
cd adventure-backend
./mvnw test
```

Frontend tests (if present):

```bash
cd adventure-frontend
npm test
```

## Design decisions / trade-offs

- Keep validation and game rules on the backend to ensure consistent behaviour across clients.
- JSON-driven book format simplifies extending the dataset without changing code.
- Maven Wrapper included so contributors don't need a system Maven install.

## Project structure

```
adventure-app/
├── README.md
├── postman_collection.json
├── adventure-backend/
└── adventure-frontend/
```

## Author

Alaa Turki

Senior Full-Stack Developer

## Documentation

- Backend: adventure-backend/README.md
- Frontend: adventure-frontend/README.md
