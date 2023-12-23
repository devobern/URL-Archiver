package ch.bfh.helper;

import ch.bfh.exceptions.ConfigFileException;
import ch.bfh.model.ConfigModel;
import ch.bfh.model.SupportedBrowsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for ConfigFileHelper to ensure correct handling of configuration files.
 */
class ConfigFileHelperTest {

    private final String invalidConfigFilePath = "nonexistent/path/config.json";
    @TempDir
    Path tempDir;
    private String validConfigFilePath;

    /**
     * Sets up the test environment with a valid file path for each test.
     */
    @BeforeEach
    void setup() {
        validConfigFilePath = tempDir.resolve("config.json").toString();
        ConfigFileHelper.setConfigFilePath(validConfigFilePath);
    }

    /**
     * Verifies that a ConfigFileException is thrown when the file is not found.
     */
    @Test
    void configFileNotFound() {
        ConfigFileHelper.setConfigFilePath(invalidConfigFilePath);
        assertThrows(ConfigFileException.class, ConfigFileHelper::read);
    }

    /**
     * Ensures ConfigFileException is thrown on IOException during save operation.
     */
    @Test
    void saveWithIOException() {
        ConfigFileHelper.setConfigFilePath(invalidConfigFilePath);
        ConfigModel config = new ConfigModel();

        assertThrows(ConfigFileException.class, () -> ConfigFileHelper.save(config));
    }

    /**
     * Tests reading of a valid configuration, asserting correct retrieval of values.
     */
    @Test
    void readValidConfig() throws IOException, ConfigFileException {
        String jsonContent = "{\"accessKey\":\"testAccessKey\", \"secretKey\":\"testSecretKey\", \"browser\":\"CHROME\"}";
        Files.writeString(tempDir.resolve("config.json"), jsonContent);

        ConfigModel configModel = ConfigFileHelper.read();
        assertNotNull(configModel);
        assertEquals("testAccessKey", configModel.getAccessKey());
        assertEquals(SupportedBrowsers.CHROME, configModel.getBrowser());
    }

    /**
     * Confirms that a valid configuration is saved and file content matches expectations.
     */
    @Test
    void saveValidConfig() throws ConfigFileException, IOException {
        ConfigModel config = new ConfigModel();
        config.setAccessKey("testAccessKey");
        config.setSecretKey("testSecretKey");
        config.setBrowser(SupportedBrowsers.CHROME);

        ConfigFileHelper.save(config);

        File configFile = new File(validConfigFilePath);
        assertTrue(configFile.exists());
        String fileContent = Files.readString(configFile.toPath());
        assertTrue(fileContent.contains("testAccessKey"));
        assertTrue(fileContent.contains("CHROME"));
    }

    /**
     * Asserts the correct browser is returned for a valid configuration.
     */
    @Test
    void getSupportedBrowser() throws IOException, ConfigFileException {
        String jsonContent = "{\"accessKey\":\"testAccessKey\", \"secretKey\":\"testSecretKey\", \"browser\":\"CHROME\"}";
        Files.writeString(Path.of(validConfigFilePath), jsonContent);

        SupportedBrowsers browser = ConfigFileHelper.getBrowser();
        assertEquals(SupportedBrowsers.CHROME, browser);
    }

    /**
     * Tests that an unsupported browser string defaults to the UNSUPPORTED enum.
     */
    @Test
    void getUnsupportedBrowser() throws IOException, ConfigFileException {
        String jsonContent = "{\"accessKey\":\"testAccessKey\", \"secretKey\":\"testSecretKey\", \"browser\":\"UNKNOWN\"}";
        Files.writeString(Path.of(validConfigFilePath), jsonContent);

        SupportedBrowsers browser = ConfigFileHelper.getBrowser();
        assertEquals(SupportedBrowsers.UNSUPPORTED, browser);
    }

}
