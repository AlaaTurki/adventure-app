
# Adventure App — Backend

The `adventure-backend` module is the backend REST API of the Adventure App.

It is implemented using **Java, Spring Boot, and Maven** and is responsible for the application's business logic, adventure-book management, validation, game navigation, consequences, and player health.

## Technologies

* Java
* Spring Boot
* Maven
* REST API
* Jackson
* JSON
* JUnit

## Responsibilities

The backend is responsible for:

* Loading adventure books
* Deserializing JSON book files
* Validating adventure books
* Providing book APIs
* Searching and filtering books
* Starting games
* Managing game state
* Navigating between sections
* Applying health consequences
* Detecting winning and losing states

## Book Model

An adventure book contains:

```text
Book
├── id
├── title
├── author
├── difficulty
└── sections
```

Each section contains:

```text
Section
├── id
├── text
├── type
└── options
```

An option contains:

```text
Option
├── description
├── gotoId
└── consequence
```

A consequence can modify the player's health.

## Supported Section Types

The application supports the following section types:

```text
BEGIN
NODE
END
```

`BEGIN` identifies the starting point of an adventure.

`NODE` represents an intermediate section.

`END` represents the end of an adventure.

## Book Validation

Books are validated before being used.

The validation rules are:

### Exactly one BEGIN

```text
BEGIN count == 1
```

A book with zero or multiple `BEGIN` sections is invalid.

### At least one END

```text
END count >= 1
```

A book must have at least one ending.

### Valid navigation

Every option must reference an existing section:

```text
gotoId → existing section
```

### Options on non-ending sections

Every section that is not an `END` must contain at least one option.

## Game Logic

A new game starts with:

```text
health = 10
status = PLAYING
```

The current section is initialized with the book's `BEGIN` section.

When the player selects an option:

```text
Player choice
      ↓
Find selected option
      ↓
Apply consequence
      ↓
Update health
      ↓
Navigate using gotoId
      ↓
Check game status
```

## Health Consequences

The JSON format supports health consequences such as:

```json
{
  "type": "HEALTH",
  "value": "-10",
  "text": "The alarm sounds as you run."
}
```

Positive values increase health:

```text
+5
+10
```

Negative values decrease health:

```text
-5
-10
-15
```

Health is prevented from becoming negative.

If:

```text
health == 0
```

the game status becomes:

```text
DEAD
```

If the player reaches an `END` section, the game status becomes:

```text
WON
```

## API

The backend exposes REST endpoints for books and gameplay.

Typical operations include:

```text
GET  /books
GET  /books/{id}
GET  /books/title/{title}
GET  /books/{bookId}/sections/{sectionId}
POST /books

POST /games/start?bookId={bookId}
GET  /games/{gameId}
POST /games/{gameId}/choices
POST /games/{gameId}/save
GET  /games/saved
```

The exact endpoints should be considered together with the controller implementation.

## Project Structure

The backend follows a layered architecture:

```text
src/main/java/com/pictet/adventure/

├── controller/
│   ├── BookController
│   └── GameController
│
├── service/
│   ├── BookService
│   ├── BookValidationService
│   └── GameService
│
├── model/
│   ├── Book
│   ├── Section
│   ├── Option
│   ├── Consequence
│   └── Game
│
├── repository/
│
├── dto/
│
├── exception/
│
└── AdventureApplication
```

The exact package structure may evolve as the implementation progresses.

## Running the Backend

From the backend directory:

### Linux / macOS

```bash
./mvnw spring-boot:run
```

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

## Build

```bash
./mvnw clean package
```

Windows:

```powershell
.\mvnw.cmd clean package
```

## Tests

Run the test suite with:

```bash
./mvnw test
```

Windows:

```powershell
.\mvnw.cmd test
```

## Configuration

Application-specific configuration is located under:

```text
src/main/resources/
```

Adventure book JSON files are stored in the resources area and loaded by the backend.

## Design Principles

The backend aims to maintain:

* Separation of concerns
* Small and focused services
* Business logic inside the service layer
* REST controllers focused on HTTP concerns
* Explicit validation
* Testable game logic
* Clear domain models

The main goal is to keep the adventure rules independent from the HTTP layer so that the core game logic can be tested independently.
