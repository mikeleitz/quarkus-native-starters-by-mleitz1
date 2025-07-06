# Quarkus Native Hello World

A simple Hello World application built with Quarkus and configured for native image compilation.

To build this project you need to have the GraalVM installed w/native image compliation.

I compiled this originally w/ Oracle GraalVM 21.0.2+13.1 (build 21.0.2+13-LTS-jvmci-23.1-b30)

* [GraalVM](https://www.graalvm.org/downloads/#)

## Features

- Built with Quarkus 3.6.4 (latest version)
- Configured for native image compilation
- Cross-platform support for:
  - macOS (x86_64 and ARM64)
  - Linux Raspberry Pi 4 and 5 (ARM64)
- Simple REST API endpoint

## Prerequisites

- JDK 17 or later
- GraalVM 22.3 or later with native-image installed (for native builds)

## Building the Application

NOTE: There isn't a JVM mode/jar file produced by default. Consider this repo having HARD CODED native build only.

### Native Mode (Current Platform)

```bash
# This is all you have to do to kick off a native build - ofc you need to have GraalVM with the Native Image component
./gradlew build
```

This will produce a native executable in `build/`.

### Native Mode for Raspberry Pi 4/5

```bash
./gradlew build -Dquarkus.profile=raspberry -Dquarkus.package.type=native
```

This will use Docker to cross-compile a native executable for ARM64 architecture.

## Running the Application

### JVM Mode

```bash
java -jar build/quarkus-app/quarkus-run.jar
```

### Native Mode

```bash
./build/example-quarkus-native-basic-0.1.0-SNAPSHOT-runner
```

## Testing the Application

Once the application is running, you can test it with:

```bash
curl http://localhost:8080/hello
```

You should receive: `Hello from Quarkus Native!`

## Running Tests

```bash
./gradlew test
```

## Project Structure

- `src/main/java/org/example/GreetingResource.java` - REST endpoint
- `src/main/resources/application.properties` - Quarkus configuration
- `src/test/java/org/example/GreetingResourceTest.java` - Tests

## Configuration

The application is configured in `src/main/resources/application.properties`. Key configurations:

- Native image settings
- Cross-platform build settings
- HTTP and logging configurations

## Notes on Cross-Platform Builds

- For macOS, native builds run directly on your machine
- For Raspberry Pi, builds use Docker with an ARM64-compatible builder image
- You can switch between profiles using `-Dquarkus.profile=mac` or `-Dquarkus.profile=raspberry`

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
