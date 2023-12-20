package ch.bfh.helper;

import ch.bfh.exceptions.ConfigFileException;
import ch.bfh.model.ConfigFileMapperModel;
import ch.bfh.model.ConfigModel;
import ch.bfh.model.SupportedBrowsers;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ConfigFileHelper {
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.json";

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

    public static ConfigModel read() throws ConfigFileException {
        ConfigFileMapperModel configMapper = getConfigMapper();
        ConfigModel config = new ConfigModel();
        config.setAccessKey(configMapper.getAccessKey());
        config.setSecretKey(configMapper.getSecretKey());
        config.setBrowser(getSupportedBrowser(configMapper.getBrowser()));
        return config;
    }

    public static void save(ConfigModel config) throws ConfigFileException {
        ObjectMapper objectMapper = new ObjectMapper();
        ConfigFileMapperModel configMapper = new ConfigFileMapperModel(config.getAccessKey(), config.getSecretKey(), config.getBrowser().name());

        try {
            objectMapper.writeValue(new File(CONFIG_FILE_PATH), configMapper);
        } catch (IOException e) {
            throw new ConfigFileException("Error writing configuration: " + e.getMessage());
        }
    }

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
