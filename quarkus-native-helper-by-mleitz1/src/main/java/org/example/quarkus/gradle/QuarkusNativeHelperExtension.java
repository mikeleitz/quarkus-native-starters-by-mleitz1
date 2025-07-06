package org.example.quarkus.gradle;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;

/**
 * Extension for configuring the Quarkus Native Helper plugin.
 * This extension provides properties to configure native image builds.
 */
public abstract class QuarkusNativeHelperExtension {

    private final Project project;

    /**
     * Creates a new extension instance.
     *
     * @param project The project this extension is associated with
     */
    public QuarkusNativeHelperExtension(Project project) {
        this.project = project;
    }

    /**
     * Whether to enable native image building.
     * Default: true
     */
    public abstract Property<Boolean> getNativeEnabled();

    /**
     * Whether to build the native image in a container.
     * Default: false
     */
    public abstract Property<Boolean> getContainerBuild();

    /**
     * Whether to build the native image in a remote container.
     * Default: false
     */
    public abstract Property<Boolean> getRemoteContainerBuild();

    /**
     * Whether to only build the native image (no JAR).
     * Default: false
     */
    public abstract Property<Boolean> getNativeOnly();

    /**
     * The builder image to use for container builds.
     * Default: quay.io/quarkus/ubi-quarkus-native-image:22.0.1-java17
     */
    public abstract Property<String> getBuilderImage();

    /**
     * The maximum heap size for the native image builder.
     * Default: 4g
     */
    public abstract Property<String> getNativeImageXmx();

    /**
     * Whether to validate the native environment before building.
     * When enabled, the build will fail if GraalVM/Mandrel and native-image are not available.
     * Default: true
     */
    public abstract Property<Boolean> getValidateNativeEnvironment();
}
