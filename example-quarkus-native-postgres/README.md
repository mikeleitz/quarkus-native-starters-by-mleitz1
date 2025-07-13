# Example Quarkus Native PostgreSQL

This is an example project demonstrating how to build a Quarkus native application with PostgreSQL integration.

## Features

- Quarkus native application
- PostgreSQL integration using reactive client
- RESTful API for user management
- Mutiny for reactive programming

## Prerequisites

- JDK 17+
- GraalVM (for native compilation)
- Docker (for running PostgreSQL)
- PostgreSQL database

## Running PostgreSQL

Before running the application, you need to start a PostgreSQL database. You can use Docker for this:

```bash
docker run --name postgres-quarkus -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```

## Running the Application in Dev Mode

You can run your application in dev mode that enables live coding using:

```bash
./gradlew :example-quarkus-native-postgres:quarkusDev
```

## Packaging and Running the Application

The application can be packaged using:

```bash
./gradlew :example-quarkus-native-postgres:build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.

## Creating a Native Executable

You can create a native executable using:

```bash
./gradlew :example-quarkus-native-postgres:build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```bash
./gradlew :example-quarkus-native-postgres:build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

## API Endpoints

The application provides the following REST endpoints:

- `GET /users` - Get all users
- `GET /users/{id}` - Get a user by ID
- `POST /users` - Create a new user

Example of creating a user:

```bash
curl -X POST -H "Content-Type: application/json" -d '{"name":"John Doe","email":"john@example.com"}' http://localhost:8080/users
```

## Database Schema

The application uses a simple database schema with a single table:

```sql
CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);
```
