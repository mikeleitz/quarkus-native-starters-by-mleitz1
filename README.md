# Quarkus Native Starters

This repository hosts a collection of sample Quarkus projects specifically designed for building NATIVE executables. Each subdirectory represents a different example project, all configured to build native executables only (no fat jar).

## Purpose

The primary goal of this repository is to provide comprehensive coverage of various Quarkus options in a native-first approach. These examples demonstrate how to leverage Quarkus' capabilities for creating efficient, fast-starting, and low-memory-footprint applications through GraalVM native image compilation.

## Project Structure

Each subdirectory is a standalone Quarkus project that:
- Is configured to build only native executables
- Demonstrates a specific Quarkus feature or integration
- Includes detailed documentation on building and running the native executable
- Contains tests that verify functionality in native mode

## Examples

The repository aims to cover a wide range of Quarkus features and integrations, including:

- **Basic**: A simple REST application demonstrating the basics of Quarkus native compilation
- **REST APIs**: RESTEasy, JAX-RS, OpenAPI, and Swagger UI
- **Security**: Authentication, Authorization, and SSO integration
- **Data Access**: Hibernate ORM, Panache, and reactive SQL clients
- **Reactive Programming**: Mutiny reactive programming model
- **Messaging**: Kafka integration and batch processing
- **Cloud Native**: Kubernetes, Docker, and cloud service integrations
- **Serverless**: Functions and event-driven architectures

For a detailed roadmap of planned examples, see the [Quarkus Native Examples Roadmap](quarkus-native-examples-roadmap.md).

## Requirements

To build and run these examples, you'll need:

- GraalVM with native-image support installed
- JDK 17 or later
- Docker (for some examples)
- Gradle

## Building Native Executables

Each example includes specific instructions for building the native executable, but generally follows this pattern:

```bash
./gradlew build
```

### Building Projects from the Root Directory

This repository is set up as a multi-project Gradle build, allowing you to build any of the projects directly from the root directory. There are two ways to build the projects:

#### Method 1: Using the Project Path

You can specify the project path using the `-p` flag:

```bash
./gradlew -p quarkus-native-helper-by-mleitz1 build
./gradlew -p example-quarkus-native-basic build
```

#### Method 2: Using the Project Name (Recommended)

With the multi-project Gradle setup, you can also use the project name directly:

```bash
./gradlew :quarkus-native-helper-by-mleitz1:build
./gradlew :example-quarkus-native-basic:build
```

#### Publishing the Quarkus Native Helper Plugin

To publish the plugin to your local Maven repository:

```bash
./gradlew :quarkus-native-helper-by-mleitz1:publishToMavenLocal
```

#### Building for Raspberry Pi (ARM64)

For native build targeting Raspberry Pi (ARM64):

```bash
./gradlew :example-quarkus-native-basic:build -Dquarkus.profile=raspberry -Dquarkus.package.type=native
```

#### Listing All Projects

To see all available projects in the build:

```bash
./gradlew listProjects
```

## Testing

All examples are tested to ensure they build and run correctly as NATIVE executables with the appropriate GraalVM version. The tests verify:

1. Successful native compilation
2. Proper startup and functionality
3. Expected performance characteristics

## Contributing

Contributions are welcome! If you'd like to add a new example or improve an existing one, please:

1. Ensure your example builds a native executable only
2. Include comprehensive documentation
3. Add appropriate tests
4. Submit a pull request

### Using the Example Template

To create a new example, you can use the `example-template` directory as a starting point:

1. Copy the `example-template` directory to a new directory with a name that follows the pattern `quarkus-native-[feature]`
2. Update the `settings.gradle` file to set the appropriate project name
3. Modify the template files to implement your specific example
4. Update the README.md with details specific to your example
5. Ensure all tests pass and the native executable builds successfully

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
