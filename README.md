# PetitionVoice

**PetitionVoice** is a full-stack platform designed to encourage civic engagement by allowing users to discover, create, and sign online petitions.

The project consists of an **Angular web application**, a **Spring Boot REST backend**, a dedicated **Python desktop application for administrators**, and a **browser extension**. Application data is stored in a **MySQL database running in a Docker container**.

## Main Features

The platform provides different functionality depending on the type of user.

### Guest Users

- Browse published petitions
- Search and filter petitions
- View petition details
- Sign petitions by providing the required personal information
- Register and log in to the platform

### Authenticated Users

- Create and submit new petitions
- Sign petitions
- View previously signed petitions
- Track submitted petitions and their status
- Edit rejected petitions and resubmit them for approval
- View and update personal account information
- Access statistics related to their activity

### Administrators

Administrators are responsible for petition moderation and use a dedicated desktop application to:

- Review submitted petitions
- Approve or reject petitions
- Provide feedback for rejected petitions
- Remove inappropriate petitions

## Browser Extension

The project also includes a browser extension that provides quick access to the **most popular petition of the day**.

The extension communicates with the backend to retrieve petition information without requiring the user to navigate directly to the main web application.

## Technologies

### Backend

- Java
- Spring Boot
- Spring Security
- REST APIs
- JWT Authentication

### Frontend

- Angular
- TypeScript
- HTML
- CSS

### Desktop Application

- Python
- CustomTkinter

### Browser Extension

- TypeScript
- HTML
- CSS

### Database

- MySQL

### Infrastructure

- Docker
- Docker Compose

## Architecture

PetitionVoice follows a client-server architecture in which multiple clients communicate with the same Spring Boot backend through REST APIs.

```text

Angular Web App ────────┐
Desktop Admin App ──────┼── REST/HTTP ──> Spring Boot Backend ──> MySQL
Browser Extension ──────┘                                      (Docker)
 
```

## Authentication and Authorization

The application uses **JWT-based authentication** and **Spring Security** to protect backend resources.

After successful authentication, the client receives a JWT that is included in subsequent requests to protected REST endpoints.

Access to functionality is controlled according to the user's role and authentication status.

The application distinguishes between:

- Guest users
- Authenticated users
- Administrators

## Petition Workflow

Petitions submitted by registered users must be reviewed by an administrator before becoming publicly available.

```text
Create petition
      |
      v
   Pending
      |
      v
Administrator review
    /       \
   v         v
Approved   Rejected
   |          |
   v          v
Published   Feedback
              |
              v
         Edit & Resubmit
```

Approved petitions become available for users to view and sign.

When a petition is rejected, the administrator can provide feedback so that the author can modify and resubmit it.

## Database

The application uses **MySQL** as its relational database.

It stores information related to:

- User accounts
- User details
- Petitions
- Petition signatures
- Petition status
- Administrative information

The MySQL server runs inside a **Docker container**, providing a reproducible database environment without requiring a separate local MySQL installation.

## Running the Application

### Requirements

To run the complete project, install:

- Java
- Node.js and npm
- Python
- Docker
- Docker Compose

## 1. Clone the Repository

```bash
git clone  https://github.com/larisa-lisei/PetitionVoice.git
cd PetitionVoice
```

## 2. Start the Database

Navigate to the backend directory:

```bash
cd backend
```

Start the MySQL Docker container:

```bash
docker compose up -d
```

Docker Compose creates and starts the MySQL database environment required by the backend.

## 3. Start the Backend

From the `backend` directory, start the Spring Boot application using Maven Wrapper.

### Windows

```bash
mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

## 4. Start the Angular Frontend

Open another terminal and navigate to the frontend directory:

```bash
cd Frontend
```

Install the dependencies:

```bash
npm install
```

Start the Angular development server:

```bash
npm start
```

The web application is available by default at:

```text
http://localhost:4200
```

## Key Technical Concepts

This project demonstrates practical experience with:

- Full-stack web development
- Java and Spring Boot
- Angular and TypeScript
- RESTful API design
- MVC (Model-View-Controller) architecture
- JWT-based authentication
- Spring Security
- Role-based authorization
- Relational database design
- MySQL
- Docker for database containerization
- Python desktop application development
- Browser extension development
- Client-server architecture
