# Quarkus Native Helper Plugin

A Gradle plugin that helps make it easier to configure and build native executables with Quarkus.


## Features

- Adds helper tasks for displaying build configuration
- Will tell you if the current setup can build native
- This is non-invasive and will report only (i.e. READ ONLY) - it won't modify your current configuration

## Usage

### Adding the plugin to your build

```gradle
plugins {
    id 'com.mleitz1.quarkus.quarkus-native-helper' version '0.1.0-SNAPSHOT'
}
```

### Basic configuration

```gradle
quarkusNativeHelper {
    nativeEnabled = true
    containerBuild = false
    nativeOnly = false
    nativeImageXmx = "4g"
}
```

### Available configuration options

| Property             | Description                                             | Default                                                |
|----------------------|---------------------------------------------------------|--------------------------------------------------------|
| nativeEnabled        | Whether to enable native image building                 | true                                                   |
| containerBuild       | Whether to build the native image in a container        | false                                                  |
| remoteContainerBuild | Whether to build the native image in a remote container | false                                                  |
| nativeOnly           | Whether to only build the native image (no JAR)         | false                                                  |
| builderImage         | The builder image to use for container builds           | quay.io/quarkus/ubi-quarkus-native-image:22.0.1-java17 |
| nativeImageXmx       | The maximum heap size for the native image builder      | 4g                                                     |

### Tasks

- `displayNativeBuildConfig`: Displays the current Quarkus native build configuration

## Requirements

- Java 17 or later
- Gradle 7.0 or later
- Quarkus 3.0 or later

## License

This project is licensed under the terms of the license found in the LICENSE file in the root directory of this project.
