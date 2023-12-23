package ch.bfh.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ConfigFileMapperModel} class.
 */
public class ConfigFileMapperModelTest {

    /**
     * Test the default constructor.
     * Verifies that the default constructor initializes the properties with empty strings.
     */
    @Test
    public void testDefaultConstructor() {
        ConfigFileMapperModel model = new ConfigFileMapperModel();
        assertAll(
                () -> assertEquals("", model.getAccessKey(), "Default accessKey should be an empty string"),
                () -> assertEquals("", model.getSecretKey(), "Default secretKey should be an empty string"),
                () -> assertEquals("", model.getBrowser(), "Default browser should be an empty string")
        );
    }

    /**
     * Test the parameterized constructor with non-empty values.
     * Verifies that the constructor correctly assigns the provided values.
     */
    @Test
    public void testParameterizedConstructor() {
        String accessKey = "myAccessKey";
        String secretKey = "mySecretKey";
        String browser = "Firefox";

        ConfigFileMapperModel model = new ConfigFileMapperModel(accessKey, secretKey, browser);
        assertAll(
                () -> assertEquals(accessKey, model.getAccessKey(), "The accessKey should match the provided value"),
                () -> assertEquals(secretKey, model.getSecretKey(), "The secretKey should match the provided value"),
                () -> assertEquals(browser, model.getBrowser(), "The browser should match the provided value")
        );
    }

    /**
     * Test the parameterized constructor with null values.
     * Verifies that the constructor treats null values as empty strings.
     */
    @Test
    public void testParameterizedConstructorWithNullValues() {
        ConfigFileMapperModel model = new ConfigFileMapperModel(null, null, null);
        assertAll(
                () -> assertNotNull(model.getAccessKey(), "Access key should not be null"),
                () -> assertNotNull(model.getSecretKey(), "Secret key should not be null"),
                () -> assertNotNull(model.getBrowser(), "Browser should not be null"),
                () -> assertEquals("", model.getAccessKey(), "Null accessKey should be treated as an empty string"),
                () -> assertEquals("", model.getSecretKey(), "Null secretKey should be treated as an empty string"),
                () -> assertEquals("", model.getBrowser(), "Null browser should be treated as an empty string")
        );
    }

    /**
     * Test the getter methods with non-default values.
     * Verifies that the getter methods return the correct values set through the constructor.
     */
    @Test
    public void testGettersWithNonDefaultValues() {
        String accessKey = "customAccessKey";
        String secretKey = "customSecretKey";
        String browser = "Chrome";

        ConfigFileMapperModel model = new ConfigFileMapperModel(accessKey, secretKey, browser);
        assertAll(
                () -> assertEquals(accessKey, model.getAccessKey(), "getAccessKey should return the correct value"),
                () -> assertEquals(secretKey, model.getSecretKey(), "getSecretKey should return the correct value"),
                () -> assertEquals(browser, model.getBrowser(), "getBrowser should return the correct value")
        );
    }
}
