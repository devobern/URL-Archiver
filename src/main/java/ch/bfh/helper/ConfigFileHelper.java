package ch.bfh.helper;

import ch.bfh.exceptions.ConfigFileException;
import ch.bfh.model.ConfigFileMapperModel;
import ch.bfh.model.ConfigModel;
import ch.bfh.model.SupportedBrowsers;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Helper class for reading and writing configuration data to a JSON file.
 */
public class ConfigFileHelper {
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.json";

    /**
     * Retrieves the configuration data from a JSON file and maps it to a ConfigFileMapperModel.
     *
     * @return The configuration data as a ConfigFileMapperModel.
     * @throws ConfigFileException If there is an issue reading the configuration file or mapping the data.
     */
    private static ConfigFileMapperModel getConfigMapper() throws ConfigFileException {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        File configFile = new File(CONFIG_FILE_PATH);

        if (configFile.exists() && !configFile.isDirectory()) {
            try {
                return objectMapper.readValue(configFile, ConfigFileMapperModel.class);
            } catch (IOException e) {
                throw new ConfigFileException("Error reading configuration: " + e.getMessage());
            }
        }
        return new ConfigFileMapperModel();
    }

    /**
     * Reads and parses the configuration data from a JSON file.
     *
     * @return A ConfigModel object containing the configuration data.
     * @throws ConfigFileException If there is an error reading the configuration file.
     */
    public static ConfigModel read() throws ConfigFileException {
        ConfigFileMapperModel configMapper = getConfigMapper();
        ConfigModel config = new ConfigModel();
        config.setAccessKey(configMapper.getAccessKey());
        config.setSecretKey(configMapper.getSecretKey());
        config.setBrowser(getSupportedBrowser(configMapper.getBrowser()));
        return config;
    }

    /**
     * Saves the provided configuration data to a JSON file.
     *
     * @param config The ConfigModel object containing the configuration data to be saved.
     * @throws ConfigFileException If there is an error writing the configuration file.
     */
    public static void save(ConfigModel config) throws ConfigFileException {
        ObjectMapper objectMapper = new ObjectMapper();
        ConfigFileMapperModel configMapper = new ConfigFileMapperModel(config.getAccessKey(), config.getSecretKey(), config.getBrowser().name());

        try {
            objectMapper.writeValue(new File(CONFIG_FILE_PATH), configMapper);
        } catch (IOException e) {
            throw new ConfigFileException("Error writing configuration: " + e.getMessage());
        }
    }

    /**
     * Retrieves the supported browser type from the configuration file.
     *
     * @return The SupportedBrowsers enum representing the browser type.
     * @throws ConfigFileException If there is an error reading the configuration file.
     */
    public static SupportedBrowsers getBrowser() throws ConfigFileException {
        ConfigFileMapperModel configMapper = getConfigMapper();
        return getSupportedBrowser(configMapper.getBrowser());
    }

    private static SupportedBrowsers getSupportedBrowser(String browserName) {
        return switch (browserName.toUpperCase()) {
            case "FIREFOX" -> SupportedBrowsers.FIREFOX;
            case "EDGE" -> SupportedBrowsers.EDGE;
            case "CHROME" -> SupportedBrowsers.CHROME;
            case "", "DEFAULT" -> SupportedBrowsers.DEFAULT;
            default -> SupportedBrowsers.UNSUPPORTED;
        };
    }
}
