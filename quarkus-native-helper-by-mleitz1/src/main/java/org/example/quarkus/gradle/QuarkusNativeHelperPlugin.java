package com.mleitz1.quarkus.gradle;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskContainer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A Gradle plugin that encapsulates Quarkus native build support.
 * This plugin simplifies the configuration of Quarkus native builds
 * by providing sensible defaults and helper tasks.
 *
 * It includes native environment detection and validation to ensure
 * the build environment meets the requirements for native image building.
 */
public class QuarkusNativeHelperPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        // Create the extension for configuration
        QuarkusNativeHelperExtension extension = project.getExtensions()
                .create("quarkusNativeHelper", QuarkusNativeHelperExtension.class, project);

        // Register tasks
        registerTasks(project, extension);

        // Do recon to find all information relevant to quarkus native
        project.getExtensions().getExtraProperties().set("isQuarkusPluginApplied", (java.util.function.Supplier<Boolean>) () -> project.plugins.hasPlugin('io.quarkus'))
        project.getExtensions().getExtraProperties().set("isGraalVM", (java.util.function.Supplier<Boolean>) this::isGraalVM);
        project.getExtensions().getExtraProperties().set("isMandrel", (java.util.function.Supplier<Boolean>) this::isMandrel);
        project.getExtensions().getExtraProperties().set("isNativeCapableJVM", (java.util.function.Supplier<Boolean>) this::isNativeCapableJVM);
        project.getExtensions().getExtraProperties().set("isNativeImageAvailable", (java.util.function.Supplier<Boolean>) this::isNativeImageAvailable);
        project.getExtensions().getExtraProperties().set("getNativeJVMType", (java.util.function.Supplier<String>) this::getNativeJVMType);
        project.getExtensions().getExtraProperties().set("getDetailedJVMInfo", (java.util.function.Supplier<Map<String, Object>>) this::getDetailedJVMInfo);
        project.getExtensions().getExtraProperties().set("validateNativeEnvironment", (java.util.function.Supplier<Boolean>) this::validateNativeEnvironment);
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
                System.out.println("⚙️  Validate Native Environment: " + extension.getValidateNativeEnvironment().get());
                System.out.println("⚙️  Native JVM Type: " + getNativeJVMType());
                System.out.println("⚙️  Native Image Available: " + isNativeImageAvailable());
                System.out.println("=========================================================\n");
            });
        });

        // Register a task to check native build environment
        tasks.register("checkNativeEnvironment", task -> {
            task.setGroup("quarkus");
            task.setDescription("Checks if the current environment supports native image building");

            task.doLast(t -> {
                String jvmType = getNativeJVMType();
                Map<String, Object> detailedInfo = getDetailedJVMInfo();

                System.out.println("🔍 Native Build Environment Check:");
                System.out.println("   Current JVM: " + detailedInfo.get("vendor") + " " + detailedInfo.get("javaVersion"));
                System.out.println("   Runtime Name: " + detailedInfo.get("runtime"));
                System.out.println("   VM Name: " + detailedInfo.get("vmName"));
                System.out.println("   VM Version: " + detailedInfo.get("vmVersion"));
                System.out.println("   Java Home: " + detailedInfo.get("javaHome"));
                System.out.println("   Mandrel in Path: " + detailedInfo.get("mandrelInPath"));
                System.out.println("   Quarkus Plugin Applied: " + (isQuarkusPluginApplied() ? "✅ Applied" : "❌ Not applied"));
                System.out.println("   GraalVM: " + (isGraalVM() ? "✅ Detected" : "❌ Not detected"));
                System.out.println("   Mandrel: " + (isMandrel() ? "✅ Detected" : "❌ Not detected"));
                System.out.println("   Native Capable: " + (isNativeCapableJVM() ? "✅ " + jvmType : "❌ Not detected"));
                System.out.println("   Native Image: " + (isNativeImageAvailable() ? "✅ Available" : "❌ Not available"));

                // Show release file content for debugging
                System.out.println("\n📄 Release File Content:");
                System.out.println(detailedInfo.get("releaseContent"));

                if (!isNativeCapableJVM() || !isNativeImageAvailable()) {
                    System.out.println("\n💡 To enable native builds:");
                    System.out.println("   Option 1 - GraalVM:");
                    System.out.println("     1. Install GraalVM from https://www.graalvm.org/downloads/");
                    System.out.println("     2. Set JAVA_HOME to point to GraalVM installation");
                    System.out.println("     3. Install native-image: gu install native-image");
                    System.out.println("   Option 2 - Mandrel:");
                    System.out.println("     1. Install Mandrel from https://github.com/graalvm/mandrel/releases");
                    System.out.println("     2. Set JAVA_HOME to point to Mandrel installation");
                    System.out.println("     3. native-image is included with Mandrel");
                    System.out.println("   4. Verify: native-image --version");
                    System.out.println("\n⚠️  This project requires GraalVM or Mandrel with native-image for building.");
                } else {
                    System.out.println("\n🎉 Native build environment is ready with " + jvmType + "!");
                }
            });
        });
    }

    /**
     * Checks if the current JVM is GraalVM.
     *
     * @return true if running on GraalVM, false otherwise
     */
    public boolean isGraalVM() {
        String javaVendor = System.getProperty("java.vendor");
        String javaRuntimeName = System.getProperty("java.runtime.name");
        String javaVmName = System.getProperty("java.vm.name");

        return (javaVendor != null && javaVendor.toLowerCase().contains("graalvm")) ||
               (javaRuntimeName != null && javaRuntimeName.toLowerCase().contains("graalvm")) ||
               (javaVmName != null && javaVmName.toLowerCase().contains("graalvm"));
    }

    /**
     * Checks if the current JVM is Mandrel.
     *
     * @return true if running on Mandrel, false otherwise
     */
    public boolean isMandrel() {
        // Check system properties first
        String javaVendor = System.getProperty("java.vendor");
        String javaRuntimeName = System.getProperty("java.runtime.name");
        String javaVmName = System.getProperty("java.vm.name");
        String javaVmVersion = System.getProperty("java.vm.version");

        if ((javaVendor != null && javaVendor.toLowerCase().contains("mandrel")) ||
            (javaRuntimeName != null && javaRuntimeName.toLowerCase().contains("mandrel")) ||
            (javaVmName != null && javaVmName.toLowerCase().contains("mandrel")) ||
            (javaVmVersion != null && javaVmVersion.toLowerCase().contains("mandrel"))) {
            return true;
        }

        // Check JAVA_HOME path for mandrel
        String javaHome = System.getProperty("java.home");
        if (javaHome != null && javaHome.toLowerCase().contains("mandrel")) {
            return true;
        }

        // Check for Mandrel-specific files in JAVA_HOME
        File releaseFile = new File(javaHome, "release");
        if (releaseFile.exists()) {
            try {
                String releaseContent = org.gradle.internal.io.IoUtils.toString(new java.io.FileInputStream(releaseFile));
                if (releaseContent.toLowerCase().contains("mandrel")) {
                    return true;
                }
            } catch (Exception e) {
                // Ignore file reading errors
            }
        }

        // Check for mandrel in lib/modules file (if it exists)
        File modulesFile = new File(javaHome, "lib/modules");
        if (modulesFile.exists()) {
            try {
                // For Mandrel, the modules file might contain mandrel-specific entries
                // This is a fallback check
                File javaHomeParent = new File(javaHome).getParentFile();
                if (javaHomeParent != null && javaHomeParent.getName().toLowerCase().contains("mandrel")) {
                    return true;
                }
            } catch (Exception e) {
                // Ignore errors
            }
        }

        return false;
    }

    /**
     * Checks if the current JVM is capable of native image building.
     *
     * @return true if the JVM is GraalVM or Mandrel, false otherwise
     */
    public boolean isNativeCapableJVM() {
        return isGraalVM() || isMandrel();
    }

    /**
     * Checks if the native-image tool is available in the current environment.
     *
     * @return true if native-image is available, false otherwise
     */
    public boolean isNativeImageAvailable() {
        String javaHome = System.getProperty("java.home");
        String nativeImageExe = System.getProperty("os.name").toLowerCase().contains("windows") ? "native-image.exe" : "native-image";

        // Check in JAVA_HOME/bin
        File nativeImagePath = new File(javaHome, "bin/" + nativeImageExe);
        if (nativeImagePath.exists()) {
            return true;
        }

        // Check in PATH
        String pathVar = System.getenv("PATH");
        if (pathVar != null) {
            String[] pathDirs = pathVar.split(File.pathSeparator);
            for (String dir : pathDirs) {
                File nativeImageInPath = new File(dir, nativeImageExe);
                if (nativeImageInPath.exists()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Gets the type of native-capable JVM.
     *
     * @return "GraalVM", "Mandrel", or "Unknown"
     */
    public String getNativeJVMType() {
        if (isGraalVM()) {
            return "GraalVM";
        } else if (isMandrel()) {
            return "Mandrel";
        } else {
            return "Unknown";
        }
    }

    /**
     * Gets detailed information about the current JVM environment.
     *
     * @return a map containing detailed JVM information
     */
    public Map<String, Object> getDetailedJVMInfo() {
        String javaHome = System.getProperty("java.home");
        Map<String, Object> info = new HashMap<>();

        info.put("vendor", System.getProperty("java.vendor"));
        info.put("runtime", System.getProperty("java.runtime.name"));
        info.put("vmName", System.getProperty("java.vm.name"));
        info.put("vmVersion", System.getProperty("java.vm.version"));
        info.put("javaHome", javaHome);
        info.put("javaVersion", System.getProperty("java.version"));

        // Check if JAVA_HOME contains mandrel in path
        info.put("mandrelInPath", javaHome != null && javaHome.toLowerCase().contains("mandrel"));

        // Try to read release file
        File releaseFile = new File(javaHome, "release");
        if (releaseFile.exists()) {
            try {
                info.put("releaseContent", org.gradle.internal.io.IoUtils.toString(new java.io.FileInputStream(releaseFile)));
            } catch (Exception e) {
                info.put("releaseContent", "Could not read release file: " + e.getMessage());
            }
        } else {
            info.put("releaseContent", "No release file found");
        }

        return info;
    }

    /**
     * Validates that the current environment meets the requirements for native image building.
     *
     * @return true if the environment is valid, throws an exception otherwise
     */
    public boolean validateNativeEnvironment() {
        boolean nativeCapableJVM = isNativeCapableJVM();
        boolean nativeImageAvailable = isNativeImageAvailable();

        if (!nativeCapableJVM || !nativeImageAvailable) {
            String jvmType = getNativeJVMType();
            Map<String, Object> detailedInfo = getDetailedJVMInfo();

            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("\n        ❌ NATIVE BUILD REQUIREMENTS NOT MET\n");
            errorMessage.append("\n        This project is configured for NATIVE-ONLY builds and requires:\n");
            errorMessage.append("\n        ");
            errorMessage.append(!nativeCapableJVM ? "❌ Native-capable JVM: Not detected" : "✅ Native-capable JVM: " + jvmType + " detected");
            errorMessage.append("\n        ");
            errorMessage.append(!nativeImageAvailable ? "❌ Native Image: Not available" : "✅ Native Image: Available");
            errorMessage.append("\n");
            errorMessage.append("\n        Current Environment:");
            errorMessage.append("\n        - Java Vendor: ").append(detailedInfo.get("vendor"));
            errorMessage.append("\n        - Java Runtime: ").append(detailedInfo.get("runtime"));
            errorMessage.append("\n        - Java VM Name: ").append(detailedInfo.get("vmName"));
            errorMessage.append("\n        - Java VM Version: ").append(detailedInfo.get("vmVersion"));
            errorMessage.append("\n        - Java Version: ").append(detailedInfo.get("javaVersion"));
            errorMessage.append("\n        - Java Home: ").append(detailedInfo.get("javaHome"));
            errorMessage.append("\n        - Mandrel in Path: ").append(detailedInfo.get("mandrelInPath"));
            errorMessage.append("\n");
            errorMessage.append("\n        Release File Content:");
            errorMessage.append("\n        ").append(detailedInfo.get("releaseContent"));
            errorMessage.append("\n");
            errorMessage.append("\n        Detection Results:");
            errorMessage.append("\n        - isGraalVM(): ").append(isGraalVM());
            errorMessage.append("\n        - isMandrel(): ").append(isMandrel());
            errorMessage.append("\n        - isNativeCapableJVM(): ").append(isNativeCapableJVM());
            errorMessage.append("\n        - isNativeImageAvailable(): ").append(isNativeImageAvailable());
            errorMessage.append("\n");
            errorMessage.append("\n        To fix this:");
            errorMessage.append("\n        1. Install GraalVM from https://www.graalvm.org/downloads/");
            errorMessage.append("\n           OR");
            errorMessage.append("\n           Install Mandrel from https://github.com/graalvm/mandrel/releases");
            errorMessage.append("\n        2. Set JAVA_HOME to point to GraalVM/Mandrel installation");
            errorMessage.append("\n        3. Install native-image: gu install native-image (GraalVM) or use built-in (Mandrel)");
            errorMessage.append("\n        4. Verify with: native-image --version");

            throw new GradleException(errorMessage.toString());
        }

        return true;
    }
}
