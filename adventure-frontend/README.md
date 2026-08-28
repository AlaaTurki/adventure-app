# Adventure App — Frontend

The `adventure-frontend` module is the Angular client application for Adventure App.

It provides the user interface for browsing adventure books and playing interactive stories through the Spring Boot REST API.

## Technologies

* Angular
* TypeScript
* HTML5
* CSS3
* Bootstrap
* Angular Router
* HttpClient

## Main Features

### Adventure Library

The home page provides access to the available adventure books.

Users can:

* Browse books
* Search books
* Filter books
* View book information
* Start an adventure

### Game Screen

The game interface displays:

* Current book name
* Player health
* Current section text
* Available choices
* Game status

The user selects a choice to continue the adventure.

```text
Current Story
      ↓
Available Choices
      ↓
User selects a choice
      ↓
Backend processes the choice
      ↓
Next section
```

### Health

The game starts with:

```text
❤️ 10 HP
```

Health changes according to consequences returned by the backend.

For example:

```text
Health: 10
     ↓
Choice: Fight the beast
     ↓
Consequence: -15
     ↓
Health: 0
     ↓
Game Over
```

### Game Completion

The frontend handles the two main game-ending states:

```text
WON
DEAD
```

When the player reaches an `END` section, the adventure is completed.

When health reaches zero, the player dies and the adventure is over.

## Project Structure

The Angular application follows a component/service-based structure:

```text
src/
│
├── app/
│   │
│   ├── components/
│   │   ├── home/
│   │   ├── book-list/
│   │   ├── book-card/
│   │   ├── game/
│   │   └── game-result/
│   │
│   ├── models/
│   │   ├── book.model.ts
│   │   ├── section.model.ts
│   │   ├── option.model.ts
│   │   ├── consequence.model.ts
│   │   └── game.model.ts
│   │
│   ├── services/
│   │   ├── book.service.ts
│   │   └── game.service.ts
│   │
│   └── app.routes.ts
│
├── assets/
│
├── styles.css
└── index.html
```

The exact structure may vary according to the current implementation.

## Communication with the Backend

The frontend communicates with the Spring Boot backend using HTTP requests.

Typical operations include:

```text
GET  /api/books
GET  /api/books/{id}

POST /api/games
GET  /api/games/{gameId}
POST /api/games/{gameId}/choices
```

Angular services are responsible for communication with the backend.

Components focus primarily on presentation and user interaction.

## Routing

The application can use Angular routing to separate the main views:

```text
/                 → Adventure Library
/game/:id         → Active Game
```

The routing structure can be extended for additional features such as saved games or adding books.

## Installation

Make sure Node.js and npm are installed.

From the frontend directory:

```bash
npm install
```

## Development Server

Run:

```bash
ng serve
```

The application will normally be available at:

```text
http://localhost:4200
```

## Production Build

Create a production build with:

```bash
ng build
```

The generated files are placed in the Angular distribution directory.

## Backend Connection

The frontend expects the Spring Boot backend to be running.

For local development, the backend and frontend run independently:

```text
Angular
http://localhost:4200
        │
        │ HTTP
        ▼
Spring Boot
http://localhost:8080
```

If the backend uses a different port, update the frontend API configuration accordingly.

## User Flow

The main user journey is:

```text
Home Page
    │
    ▼
Browse Adventure Books
    │
    ├── Search
    │
    └── Filter
    │
    ▼
Select Book
    │
    ▼
Start Game
    │
    ▼
Display BEGIN Section
    │
    ▼
Choose an Action
    │
    ▼
Apply Consequence
    │
    ▼
Update Health
    │
    ▼
Display Next Section
    │
    ├───────────────┐
    │               │
    ▼               ▼
   END             0 HP
    │               │
    ▼               ▼
   WON             DEAD
```

## UI Principles

The frontend is designed around:

* Simple navigation
* Clear presentation of story content
* Visible player health
* Clearly distinguishable choices
* Responsive layout
* Separation between library and gameplay
* Clear game completion states

## Development

Recommended development workflow:

```bash
npm install
ng serve
```

The backend should be started separately.

During development, changes to Angular source files are automatically reflected in the browser.

## Assessment Objectives

The frontend supports the assessment objectives in order:

1. Home page with adventure library
2. Search and filtering
3. Start and play an adventure
4. Display choices and navigate through sections
5. Display health and consequences
6. Display game completion/death states
7. Optional progression persistence
8. Optional book creation

The implementation prioritizes the core objectives before optional feature

# Adventure Frontend

Angular frontend for the adventure book application.

## Overview

This app lets the player browse adventure books and navigate through branching story choices.

## Prerequisites

- Node.js 18+
- npm
- Backend running at http://localhost:8080/api

## Quick start

```bash
cd adventure-frontend
npm install
npm start
```

Open the app in the browser at:

- http://localhost:4200

## Build

```bash
npm run build
```

## Angular version

```bash
Angular CLI: 20.3.31
Node: 22.13.1
Package Manager: npm 10.9.2
```

## Project structure

- src/
  - app/
    - components/
    - models/
    - services/
    - app-routing.module.ts
    - app.module.ts
  - index.html
  - main.ts
  - styles.css

## API integration

The frontend calls the backend at:

- `http://localhost:8080/api/books`

## Troubleshooting

- If the app cannot connect to the backend, verify the backend is running on port 8080
- If there are stale red squiggles in VS Code or IntelliJ, do a clean IDE refresh; the Angular build is known to compile successfully
- If the app loads no books, confirm the backend has loaded data from `adventure-backend/files/books`
