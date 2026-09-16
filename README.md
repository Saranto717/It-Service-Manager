# IT Service Manager

REST API for managing IT service requests, users and technicians.

## 🚀 Overview

IT Service Manager is a Spring Boot REST API designed for managing IT support tickets.

The application provides:

- User management
- Ticket creation and management
- Technician assignment
- Ticket status management
- Role-based authorization
- JWT authentication
- PostgreSQL persistence
- Request validation
- Global exception handling
- Swagger / OpenAPI documentation

## 🛠️ Technologies

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Maven
- JUnit 5
- Mockito
- MockMvc
- Swagger / OpenAPI

## 🔐 Authentication & Authorization

The application uses JWT-based authentication.

Available roles:

- `USER`
- `TECHNICIAN`
- `ADMIN`

Authorization rules are implemented with Spring Security.

Examples:

- Users can create tickets.
- Authenticated users can view tickets.
- Technicians can assign tickets.
- Technicians can update ticket status.
- Administrators can manage users.

## 🎫 Ticket Management

Tickets contain:

- Title
- Description
- Priority
- Status
- Creator
- Assigned technician
- Creation date

Available ticket statuses:

- `OPEN`
- `IN_PROGRESS`
- `RESOLVED`
- `CLOSED`

Available priorities:

- `LOW`
- `MEDIUM`
- `HIGH`
- `CRITICAL`

## 📚 API Documentation

Swagger UI is available at:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

OpenAPI specification:

[http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## 🧪 Testing

The project includes unit and controller tests using:

- JUnit 5
- Mockito
- Spring MockMvc

Current test suite:

- 22 tests
- 22 passed
- 0 failures
- 0 errors

## ⚙️ Configuration

Database and JWT configuration are provided through environment variables.

Required variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET


Example:

DB_URL=jdbc:postgresql://localhost:5432/it_service_management
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_secret
▶️ Running the Application

Make sure PostgreSQL is running and the required environment variables are configured.

Then run:

./mvnw spring-boot:run

On Windows:

mvnw.cmd spring-boot:run

The application runs on:

http://localhost:8080

📁 Project Structure
src
├── main
│   ├── java/com/andreas/itservicemanager
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── exception
│   │   ├── repository
│   │   ├── security
│   │   └── service
│   └── resources
│       └── application.properties
│
└── test
    └── java/com/andreas/itservicemanager
        ├── controller
        └── service
📌 API Endpoints
Authentication
POST /api/auth/login
Users
POST   /api/users
GET    /api/users
GET    /api/users/{id}
PUT    /api/users/{id}
DELETE /api/users/{id}
Tickets
POST /api/tickets
GET /api/tickets
GET /api/tickets/{id}
PUT /api/tickets/{id}/assign
PUT /api/tickets/{id}/status
👨‍💻 Author

Andreas Sarantopoulos

GitHub:

https://github.com/Saranto717