package com.mleitz1.quarkus.gradle;

import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Additional tests for Mleitz1QuarkusPropertyResolver.
 * This test class focuses on testing the methods that have logic involved.
 */
public class Mleitz1QuarkusPropertyResolverTest {

    private TestablePropertyResolver resolver;
    private Map<String, Object> testProperties;

    @BeforeEach
    void setUp() {
        testProperties = new HashMap<>();
        resolver = new TestablePropertyResolver(testProperties);
    }

    @AfterEach
    void tearDown() {
        // Clear any system properties that were set during tests
        System.clearProperty("quarkus.test.property");
        System.clearProperty("quarkus.test.string.property");
    }

    @Test
    void testFormatBooleanValue_True() {
        // Test
        String result = resolver.formatBooleanValue(true);

        // Verify
        assertEquals("enabled", result);
    }

    @Test
    void testFormatBooleanValue_False() {
        // Test
        String result = resolver.formatBooleanValue(false);

        // Verify
        assertEquals("disabled", result);
    }

    @Test
    void testGetSystemPropertyValue_PropertyExists() {
        // Setup
        System.setProperty("quarkus.test.property", "true");

        // Test
        Boolean result = resolver.getSystemPropertyValue("quarkus.test.property");

        // Verify
        assertEquals(true, result);
    }

    @Test
    void testGetSystemPropertyValue_PropertyDoesNotExist() {
        // Test
        Boolean result = resolver.getSystemPropertyValue("quarkus.nonexistent.property");

        // Verify
        assertNull(result);
    }

    @Test
    void testGetSystemPropertyValue_PropertyIsFalse() {
        // Setup
        System.setProperty("quarkus.test.property", "false");

        // Test
        Boolean result = resolver.getSystemPropertyValue("quarkus.test.property");

        // Verify
        assertEquals(false, result);
    }

    @Test
    void testGetStringPropertyStatus_SystemNullGradleSet() {
        // Setup
        testProperties.put("quarkus.test.string.property", "test-value");

        // Test
        String result = resolver.getStringPropertyStatus("quarkus.test.string.property");

        // Verify
        assertEquals("test-value", result);
    }

    @Test
    void testGetStringPropertyStatus_SystemNullGradleNull() {
        // Test
        String result = resolver.getStringPropertyStatus("quarkus.test.string.property");

        // Verify
        assertEquals("not set", result);
    }

    @Test
    void testGetStringPropertyStatus_SystemSetGradleNull() {
        // Setup
        System.setProperty("quarkus.test.string.property", "system-value");

        // Test
        String result = resolver.getStringPropertyStatus("quarkus.test.string.property");

        // Verify
        assertEquals("⚠️ Mismatch: System=system-value, Gradle=not set", result);
    }

    @Test
    void testGetStringPropertyStatus_SystemSetGradleSetEqual() {
        // Setup
        testProperties.put("quarkus.test.string.property", "same-value");
        System.setProperty("quarkus.test.string.property", "same-value");

        // Test
        String result = resolver.getStringPropertyStatus("quarkus.test.string.property");

        // Verify
        assertEquals("same-value", result);
    }

    @Test
    void testGetStringPropertyStatus_SystemSetGradleSetDifferent() {
        // Setup
        testProperties.put("quarkus.test.string.property", "gradle-value");
        System.setProperty("quarkus.test.string.property", "system-value");

        // Test
        String result = resolver.getStringPropertyStatus("quarkus.test.string.property");

        // Verify
        assertEquals("⚠️ Mismatch: System=system-value, Gradle=gradle-value", result);
    }

    /**
     * A testable subclass of Mleitz1QuarkusPropertyResolver that overrides
     * the methods that interact with the Project interface.
     */
    private static class TestablePropertyResolver extends Mleitz1QuarkusPropertyResolver {
        private final Map<String, Object> properties;

        public TestablePropertyResolver(Map<String, Object> properties) {
            super(null); // Pass null as we're overriding the methods that use Project
            this.properties = properties;
        }

        @Override
        protected Boolean getGradlePropertyValue(String propertyName) {
            Object value = properties.get(propertyName);
            if (value == null) {
                return false;
            }
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            return Boolean.parseBoolean(value.toString());
        }

        @Override
        protected String getGradlePropertyStringValue(String propertyName) {
            Object value = properties.get(propertyName);
            if (value == null) {
                return null;
            }
            return value.toString();
        }

        // Make protected methods public for testing
        public String formatBooleanValue(Boolean value) {
            return super.formatBooleanValue(value);
        }

        public Boolean getSystemPropertyValue(String propertyName) {
            return super.getSystemPropertyValue(propertyName);
        }

        public String getStringPropertyStatus(String propertyName) {
            return super.getStringPropertyStatus(propertyName);
        }
    }
}
