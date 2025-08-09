# Quarkus Native Starters

A collection of starter projects and tools for building native executables with Quarkus. This repository provides example projects and configuration to simplify the development and building of Quarkus native applications.

[![Quarkus](https://img.shields.io/badge/Quarkus-3.6.4-blue)](https://quarkus.io/)
[![GraalVM](https://img.shields.io/badge/GraalVM-Compatible-green)](https://www.graalvm.org/)
[![Mandrel](https://img.shields.io/badge/Mandrel-Compatible-green)](https://github.com/graalvm/mandrel)

## Overview

This project provides:

1. **Example Projects** - Ready-to-use Quarkus native application examples
2. **Build Configuration** - Pre-configured Gradle setup for native builds

## Quick Start

```bash
# Clone the repository
git clone https://github.com/yourusername/quarkus-native-starters-by-mleitz1.git
cd quarkus-native-starters-by-mleitz1

# Build everything (examples)
./gradlew build

# Run the basic example
cd example-quarkus-native-basic/build
chmod +x ./example-quarkus-native-basic-0.1.0-runner
./example-quarkus-native-basic-0.1.0-runner
```

## Prerequisites

To build native executables, you need either:

- **GraalVM** (JDK 17+) with `native-image` installed
- **Mandrel** (JDK 17+)


## Project Structure

- **example-quarkus-native-basic** - Minimal Quarkus application with native build configuration


## Native Build Configuration

The repository is configured for native builds by default with these settings in `gradle.properties`:

```properties
# Force native build
quarkus.native.enabled=true
quarkus.package.jar.enabled=false

# Native image configuration
quarkus.native.container-build=false
quarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-native-image:22.0.1-java17
quarkus.native.native-image-xmx=24g
```

## Building the Project

This setup works out of the box! You can run:

```bash
./gradlew build
```

This will build all the subprojects. The native build will work if your environment is set up correctly with GraalVM or Mandrel.


## Troubleshooting

If you encounter issues with native builds:

- Ensure you have GraalVM or Mandrel properly installed and configured
- Check that `native-image` is available in your PATH
- Review the Gradle build output for diagnostic details

Common issues:
- Incorrect JAVA_HOME setting
- Missing native-image tool
- Insufficient memory for native image compilation

## Examples

### Reactive Architecture

Both example projects in this repository are built using Quarkus' reactive architecture:

- **RESTEasy Reactive** - All REST endpoints use Quarkus' reactive HTTP engine
- **Non-blocking I/O** - Designed for high concurrency with minimal resource usage
- **Reactive Programming Model** - Using Mutiny for asynchronous and non-blocking operations

This reactive foundation provides several benefits:
- Improved resource utilization
- Better scalability under load
- Reduced memory footprint in native executables
- More efficient handling of concurrent requests

### Basic Quarkus Native Application

The `example-quarkus-native-basic` project demonstrates:

- Minimal Quarkus reactive REST application
- Native build configuration

To run:

```bash
cd example-quarkus-native-basic
../gradlew build
chmod +x ./build/example-quarkus-native-basic-0.1.0-runner
./build/example-quarkus-native-basic-0.1.0-runner
```

### PostgreSQL Quarkus Native Application

The `example-quarkus-native-postgres` project demonstrates:

- Fully reactive PostgreSQL integration
- Mutiny reactive types (Uni and Multi) for database operations
- Non-blocking REST API with reactive endpoints
- Native build configuration with database access

To run:

```bash
# Start PostgreSQL (if not already running)
docker run --name postgres-quarkus -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres

# Build and run the application
cd example-quarkus-native-postgres
../gradlew build
chmod +x ./build/example-quarkus-native-postgres-0.1.0-runner
./build/example-quarkus-native-postgres-0.1.0-runner
```

## License

This project is licensed under the terms of the license found in the LICENSE file in the root directory of this project.
