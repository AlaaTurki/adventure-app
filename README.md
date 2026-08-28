# Adventure App

**Adventure App** is a full-stack interactive adventure book application developed as part of a technical assessment.

The application combines a **Spring Boot REST API** backend with an **Angular** frontend to provide an interactive adventure-book experience. Users can browse available books, search and filter the library, start an adventure, make choices, and progress through different story sections.

Players begin each adventure with **10 health points**. Depending on their choices, consequences can modify their health. The adventure ends when the player reaches an ending section or their health reaches zero.

## Project Structure

```text
adventure-app/
│
├── adventure-backend/
│   ├── src/
│   ├── pom.xml
│   └── README.md
│
├── adventure-frontend/
│   ├── src/
│   ├── package.json
│   └── README.md
│
├── .gitignore
└── README.md
```

## Architecture

```text
┌─────────────────────────────┐
│       Angular Frontend      │
│                             │
│  Book Library               │
│  Search & Filter            │
│  Game Interface             │
│  Health & Choices           │
└──────────────┬──────────────┘
               │ HTTP / REST
               ▼
┌─────────────────────────────┐
│      Spring Boot API        │
│                             │
│  Book Management            │
│  Book Validation            │
│  Game Management            │
│  Navigation                 │
│  Consequences               │
│  Health Management          │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│      Adventure Books        │
│          JSON Data          │
└─────────────────────────────┘
```

## Main Features

### Book Library

* Display available adventure books
* Search books
* Filter books by difficulty
* Display book information such as title, author, and difficulty

### Interactive Gameplay

* Start a new adventure
* Start from the book's `BEGIN` section
* Display the current section
* Display available choices
* Navigate to the selected section
* Detect ending sections

### Health & Consequences

Each player starts with:

```text
10 health points
```

Choices may contain health consequences such as:

```json
{
  "type": "HEALTH",
  "value": "-5",
  "text": "You are injured."
}
```

or:

```json
{
  "type": "HEALTH",
  "value": "+5",
  "text": "You feel stronger."
}
```

When the player's health reaches zero, the adventure ends.

## Book Validation

Books are validated before they can be used by the application.

According to the assessment requirements, a book is invalid when:

* It has no `BEGIN` section
* It has more than one `BEGIN` section
* It has no `END` section
* An option references a non-existing section
* A non-ending section has no options

Multiple `END` sections are allowed.

## Technologies

### Backend

* Java
* Spring Boot
* Maven
* REST API
* Jackson
* JSON

### Frontend

* Angular
* TypeScript
* HTML
* CSS
* Bootstrap

## Running the Application

### 1. Start the backend

```bash
cd adventure-backend
./mvnw spring-boot:run
```

On Windows:

```powershell
cd adventure-backend
.\mvnw.cmd spring-boot:run
```

### 2. Start the frontend

Open another terminal:

```bash
cd adventure-frontend
npm install
ng serve
```

The Angular application will normally be available at:

```text
http://localhost:4200
```

The backend runs on the configured Spring Boot port.

## Testing

Backend tests can be executed with:

```bash
cd adventure-backend
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

## Assessment Objectives

The implementation follows the objectives in the requested order:

1. **Home page** — book library, search, and filtering
2. **Basic gameplay** — start an adventure and navigate between sections
3. **Consequences** — health management and game completion
4. **Save progression** — additional objective
5. **Add new books** — additional objective

The project prioritizes the core objectives before the optional features.

## Design Approach

The project separates responsibilities between the frontend and backend.

The backend contains the business rules and game logic, while the Angular application focuses on presentation and user interaction.

This separation makes the application easier to test, maintain, and extend.

## Author

**Alaa Turki**

Senior Full-Stack Develop

# Adventure Book Full Stack

This project is a full-stack choose-your-own-adventure application built with Spring Boot on the backend and Angular on the frontend. The application presents a collection of interactive adventure books, each composed of branching sections, choices, and consequences. The user can browse available stories, open a selected book, and move through the narrative by choosing different paths.

The project is designed to demonstrate a complete web application lifecycle: modeling the domain, persisting data, exposing a REST API, and building an interactive frontend experience. It is also structured to be easy to run locally and explain during a technical interview.

## Repository

- GitHub: https://github.com/AlaaTurki/adventure-app.git

## Project overview

The application is split into two main parts:

- Backend: Java 21 + Spring Boot + Maven + H2 database
- Frontend: Angular + TypeScript + Bootstrap + REST API integration

The backend loads adventure content from JSON files and stores it in an in-memory database. The frontend calls the backend to retrieve the available books and the current story state, then renders the story and navigation options to the player.

This creates a clean client-server architecture where the backend manages data and logic, while the frontend focuses on user interaction and presentation.

## Project structure

- adventure-app/
  - adventure-backend/
    - src/
    - files/books/
    - pom.xml
    - README.md
    - .gitignore
  - adventure-frontend/
    - src/
    - package.json
    - README.md
    - .gitignore
  - README.md
  - .gitignore
  - .vscode/

## Features

- Browse a list of adventure books
- View each book's title, author, and difficulty
- Open a story and read the current section content
- Choose from multiple options that lead to new sections
- Support consequences such as health gain or health loss
- Load all data from JSON files at startup
- Expose a REST API for frontend consumption
- Use an H2 database for quick local development

## Prerequisites

- Java 21+
- Maven or Maven wrapper
- Node.js 22+
- npm

## Run the backend

```bash
cd adventure-app/adventure-backend
./mvnw.cmd clean install
./mvnw.cmd spring-boot:run
```

The backend runs on:

- http://localhost:8080/api
- H2 console: http://localhost:8080/api/h2-console/

## Run the frontend

```bash
cd adventure-app/adventure-frontend
npm install
npm start
```

Open the app here:

- http://localhost:4200

## API endpoints

```text
GET    /api/books
GET    /api/books/{id}
GET    /api/books/title/{title}
GET    /api/books/{bookId}/sections/{sectionId}
POST   /api/books
```

## Notes

- The backend auto-loads story data from `adventure-backend/files/books` at startup.
- The frontend calls the backend using `http://localhost:8080/api/books`.
- The app is designed for local demo and interview use, with an in-memory database to keep setup simple.
- If the IDE shows red Java classes, refresh or reimport the Maven project and run a compile command. The backend compiles successfully.

## Documentation

- Backend: [adventure-backend/README.md](adventure-backend/README.md)
- Frontend: [adventure-frontend/README.md](adventure-frontend/README.md)

## Summary

This repository demonstrates a realistic full-stack application: a Java backend exposes story data, a frontend renders interactive narrative flows, and a data-driven architecture makes content easy to extend. The project is suitable for local development, demo use, and technical interview discussion because it covers architecture, API design, persistence, debugging, and UI integration.
