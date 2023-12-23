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
 * Helper class for handling configuration data, including reading from and writing to a JSON file.
 */
public class ConfigFileHelper {
    private static final String DEFAULT_CONFIG_FILE_PATH = "src/main/resources/config.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static String configFilepath = DEFAULT_CONFIG_FILE_PATH;

    /**
     * Sets the path for the configuration file.
     *
     * @param filePath Custom path for the configuration file, falls back to default if null or blank.
     */
    public static void setConfigFilePath(String filePath) {
        configFilepath = filePath != null && !filePath.isBlank() ? filePath : DEFAULT_CONFIG_FILE_PATH;
    }

    /**
     * Reads configuration data from a JSON file and returns it as a {@link ConfigModel}.
     *
     * @return The configuration data as {@link ConfigModel}.
     * @throws ConfigFileException If reading the configuration file fails.
     */
    public static ConfigModel read() throws ConfigFileException {
        ConfigFileMapperModel configMapper = getConfigMapper();
        return mapToConfigModel(configMapper);
    }

    /**
     * Writes the provided configuration data to a JSON file. Overwrites the file if it exists.
     *
     * @param config Configuration data to save.
     * @throws ConfigFileException If writing to the configuration file fails.
     */
    public static void save(ConfigModel config) throws ConfigFileException {
        try {
            File configFile = new File(configFilepath);

            // Write the configuration data to the file. It overwrites if the file already exists.
            OBJECT_MAPPER.writeValue(configFile, mapToConfigFileMapperModel(config));
        } catch (IOException e) {
            throw new ConfigFileException("Error writing configuration: " + e.getMessage());
        }
    }

    /**
     * Retrieves the supported browser type specified in the configuration file.
     *
     * @return The supported browser as a {@link SupportedBrowsers} enum.
     * @throws ConfigFileException If reading the configuration file fails.
     */
    public static SupportedBrowsers getBrowser() throws ConfigFileException {
        return getSupportedBrowser(getConfigMapper().getBrowser());
    }

    private static ConfigFileMapperModel getConfigMapper() throws ConfigFileException {
        try {
            return OBJECT_MAPPER.readValue(getConfigFile(), ConfigFileMapperModel.class);
        } catch (IOException e) {
            throw new ConfigFileException("Error reading configuration: " + e.getMessage());
        }
    }

    private static File getConfigFile() throws ConfigFileException {
        File configFile = new File(configFilepath);

        if (!configFile.exists() || configFile.isDirectory()) {
            throw new ConfigFileException("Configuration file does not exist or is a directory: " + configFilepath);
        }

        return configFile;
    }

    private static ConfigModel mapToConfigModel(ConfigFileMapperModel configMapper) {
        ConfigModel config = new ConfigModel();
        config.setAccessKey(configMapper.getAccessKey());
        config.setSecretKey(configMapper.getSecretKey());
        config.setBrowser(getSupportedBrowser(configMapper.getBrowser()));
        return config;
    }

    private static ConfigFileMapperModel mapToConfigFileMapperModel(ConfigModel config) {
        return new ConfigFileMapperModel(
                config.getAccessKey(),
                config.getSecretKey(),
                config.getBrowser().name()
        );
    }

    private static SupportedBrowsers getSupportedBrowser(String browserName) {
        if (browserName == null || browserName.isBlank()) {
            return SupportedBrowsers.DEFAULT;
        }

        return switch (browserName.toUpperCase()) {
            case "FIREFOX" -> SupportedBrowsers.FIREFOX;
            case "EDGE" -> SupportedBrowsers.EDGE;
            case "CHROME" -> SupportedBrowsers.CHROME;
            default -> SupportedBrowsers.UNSUPPORTED;
        };
    }
}
