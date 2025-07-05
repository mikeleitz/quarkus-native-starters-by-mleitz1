# Quarkus Native Examples Roadmap

This document outlines the planned examples for the Quarkus Native Starters repository. Each example will be configured to build native executables only and will demonstrate specific Quarkus features and integrations.

## Current Examples

- **quarkus-native-basic-by-mleitz1**: A simple REST application demonstrating the basics of Quarkus native compilation

## Planned Examples

### REST and API Development

- **quarkus-native-rest-crud**: REST CRUD application with OpenAPI and Swagger UI
- **quarkus-native-graphql**: GraphQL API implementation
- **quarkus-native-reactive-routes**: Reactive routes with non-blocking handlers

### Security

- **quarkus-native-jwt-auth**: JWT authentication and authorization
- **quarkus-native-oidc**: OpenID Connect integration
- **quarkus-native-keycloak**: Keycloak integration for SSO

### Data Access

- **quarkus-native-hibernate**: Hibernate ORM with Panache
- **quarkus-native-reactive-sql**: Reactive SQL clients
- **quarkus-native-mongodb**: MongoDB integration
- **quarkus-native-redis**: Redis integration

### Reactive Programming

- **quarkus-native-mutiny**: Mutiny reactive programming model
- **quarkus-native-reactive-messaging**: Reactive messaging patterns
- **quarkus-native-reactive-streams**: Reactive streams processing

### Messaging

- **quarkus-native-kafka**: Kafka producer and consumer
- **quarkus-native-amqp**: AMQP with RabbitMQ
- **quarkus-native-kafka-streams**: Kafka Streams processing

### Cloud Native

- **quarkus-native-kubernetes**: Kubernetes deployment and integration
- **quarkus-native-openshift**: OpenShift deployment and integration
- **quarkus-native-docker**: Docker integration with multi-stage builds
- **quarkus-native-health**: Health checks and metrics

### Serverless

- **quarkus-native-lambda**: AWS Lambda integration
- **quarkus-native-azure-functions**: Azure Functions integration
- **quarkus-native-knative**: Knative integration

### Cross-Platform

- **quarkus-native-raspberry-pi**: Cross-compilation for Raspberry Pi (ARM64)
- **quarkus-native-apple-silicon**: Optimized builds for Apple Silicon (M1/M2)

### Advanced Features

- **quarkus-native-grpc**: gRPC services
- **quarkus-native-websockets**: WebSockets implementation
- **quarkus-native-scheduler**: Scheduled tasks and jobs
- **quarkus-native-fault-tolerance**: Fault tolerance with circuit breakers

## Implementation Priority

1. REST and API Development examples
2. Data Access examples
3. Security examples
4. Messaging examples
5. Reactive Programming examples
6. Cloud Native examples
7. Serverless examples
8. Cross-Platform examples
9. Advanced Features examples

## Example Structure

Each example will follow a consistent structure:

1. Clear README.md with:
   - Overview and purpose
   - Features demonstrated
   - Prerequisites
   - Build and run instructions
   - Testing instructions
   - Configuration options

2. Comprehensive tests that verify:
   - Successful native compilation
   - Proper functionality
   - Performance characteristics

3. Well-documented code with:
   - Clear comments
   - Proper error handling
   - Best practices for native image compatibility

4. Configuration for native-only builds
