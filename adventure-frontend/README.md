# Adventure Frontend

Angular frontend for the adventure book application.

## Overview

This app lets the player browse adventure books and navigate through branching story choices.

## Prerequisites

- Node.js 22+
- npm
- Backend running at http://localhost:8080

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

- `http://localhost:8080/books`

## Troubleshooting

- If the app cannot connect to the backend, verify the backend is running on port 8080
- If there are stale red squiggles in VS Code or IntelliJ, do a clean IDE refresh; the Angular build is known to compile successfully
- If the app loads no books, confirm the backend has loaded data from `adventure-backend/files/books`
