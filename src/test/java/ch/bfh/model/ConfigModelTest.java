package ch.bfh.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ConfigModel} class.
 */
class ConfigModelTest {

    /**
     * Tests the default constructor of ConfigModel.
     * Verifies that the access key, secret key, and browser are set to their default values.
     */
    @Test
    void testDefaultConstructor() {
        ConfigModel config = new ConfigModel();
        assertEquals("", config.getAccessKey(), "Default accessKey should be empty");
        assertEquals("", config.getSecretKey(), "Default secretKey should be empty");
        assertEquals(SupportedBrowsers.DEFAULT, config.getBrowser(), "Default browser should be SupportedBrowsers.DEFAULT");
    }

    /**
     * Tests the constructor of ConfigModel with access and secret key parameters.
     * Checks if the specified access and secret keys are set correctly and the browser is set to its default.
     */
    @Test
    void testConstructorWithAccessAndSecretKey() {
        ConfigModel config = new ConfigModel("access123", "secret123");
        assertEquals("access123", config.getAccessKey(), "AccessKey should match constructor argument");
        assertEquals("secret123", config.getSecretKey(), "SecretKey should match constructor argument");
        assertEquals(SupportedBrowsers.DEFAULT, config.getBrowser(), "Browser should be SupportedBrowsers.DEFAULT");
    }

    /**
     * Tests the constructor of ConfigModel with access key, secret key, and browser parameters.
     * Ensures that all parameters are set correctly in the ConfigModel instance.
     */
    @Test
    void testConstructorWithAllParameters() {
        ConfigModel config = new ConfigModel("access123", "secret123", SupportedBrowsers.CHROME);
        assertEquals("access123", config.getAccessKey(), "AccessKey should match constructor argument");
        assertEquals("secret123", config.getSecretKey(), "SecretKey should match constructor argument");
        assertEquals(SupportedBrowsers.CHROME, config.getBrowser(), "Browser should match constructor argument");
    }

    /**
     * Tests the setter and getter for the access key in ConfigModel.
     * Confirms that the setAccessKey method correctly updates the access key and getAccessKey retrieves it.
     */
    @Test
    void testSetAndGetAccessKey() {
        ConfigModel config = new ConfigModel();
        config.setAccessKey("newAccessKey");
        assertEquals("newAccessKey", config.getAccessKey(), "AccessKey should be updated by setter");
    }

    /**
     * Tests the setter and getter for the secret key in ConfigModel.
     * Validates that the setSecretKey method correctly updates the secret key and getSecretKey retrieves it.
     */
    @Test
    void testSetAndGetSecretKey() {
        ConfigModel config = new ConfigModel();
        config.setSecretKey("newSecretKey");
        assertEquals("newSecretKey", config.getSecretKey(), "SecretKey should be updated by setter");
    }

    /**
     * Tests the setter and getter for the browser preference in ConfigModel.
     * Ensures that the setBrowser method correctly updates the browser and getBrowser retrieves it.
     */
    @Test
    void testSetAndGetBrowser() {
        ConfigModel config = new ConfigModel();
        config.setBrowser(SupportedBrowsers.FIREFOX);
        assertEquals(SupportedBrowsers.FIREFOX, config.getBrowser(), "Browser should be updated by setter");
    }
}
