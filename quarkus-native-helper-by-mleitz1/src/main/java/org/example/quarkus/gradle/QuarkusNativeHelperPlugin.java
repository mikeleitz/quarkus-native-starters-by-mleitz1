package org.example.quarkus.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskContainer;

/**
 * A Gradle plugin that encapsulates Quarkus native build support.
 * This plugin simplifies the configuration of Quarkus native builds
 * by providing sensible defaults and helper tasks.
 */
public class QuarkusNativeHelperPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        // Create the extension for configuration
        QuarkusNativeHelperExtension extension = project.getExtensions()
                .create("quarkusNativeHelper", QuarkusNativeHelperExtension.class, project);

        // Apply the Quarkus plugin if it's not already applied
        if (!project.getPlugins().hasPlugin("io.quarkus")) {
            project.getPlugins().apply("io.quarkus");
        }

        // Configure the project after it has been evaluated
        project.afterEvaluate(p -> {
            configureNativeBuild(p, extension);
        });

        // Register tasks
        registerTasks(project, extension);
    }

    private void configureNativeBuild(Project project, QuarkusNativeHelperExtension extension) {
        // Set system properties for native build if enabled
        if (extension.getNativeEnabled().get()) {
            System.setProperty("quarkus.native.enabled", "true");
            System.setProperty("quarkus.package.type", "native");

            // Configure container build
            boolean containerBuild = extension.getContainerBuild().get();
            System.setProperty("quarkus.native.container-build", String.valueOf(containerBuild));

            if (containerBuild) {
                // Set builder image if specified
                extension.getBuilderImage().getOrNull().ifPresent(image ->
                    System.setProperty("quarkus.native.builder-image", image));

                // Set remote container build if enabled
                boolean remoteContainerBuild = extension.getRemoteContainerBuild().get();
                System.setProperty("quarkus.native.remote-container-build", String.valueOf(remoteContainerBuild));
            }

            // Set memory configuration if specified
            extension.getNativeImageXmx().getOrNull().ifPresent(xmx ->
                System.setProperty("quarkus.native.native-image-xmx", xmx));

            // Disable JAR packaging if native only
            if (extension.getNativeOnly().get()) {
                System.setProperty("quarkus.package.jar.enabled", "false");
            }
        }
    }

    private void registerTasks(Project project, QuarkusNativeHelperExtension extension) {
        TaskContainer tasks = project.getTasks();

        // Register a task to display native build configuration
        tasks.register("displayNativeBuildConfig", task -> {
            task.setGroup("quarkus");
            task.setDescription("Displays the current Quarkus native build configuration");

            task.doLast(t -> {
                System.out.println("\n=========================================================");
                System.out.println("QUARKUS NATIVE BUILD CONFIGURATION");
                System.out.println("=========================================================");
                System.out.println("⚙️  Native Build Enabled: " + extension.getNativeEnabled().get());
                System.out.println("⚙️  Container Build: " + extension.getContainerBuild().get());
                System.out.println("⚙️  Remote Container Build: " + extension.getRemoteContainerBuild().get());
                System.out.println("⚙️  Builder Image: " + extension.getBuilderImage().getOrElse("default"));
                System.out.println("⚙️  Native Image Memory: " + extension.getNativeImageXmx().getOrElse("default"));
                System.out.println("⚙️  Native Only (No JAR): " + extension.getNativeOnly().get());
                System.out.println("=========================================================\n");
            });
        });
    }
}
