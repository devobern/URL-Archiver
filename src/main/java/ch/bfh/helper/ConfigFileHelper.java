package ch.bfh.helper;

import ch.bfh.exceptions.ConfigFileException;
import ch.bfh.model.ConfigFileMapperModel;
import ch.bfh.model.ConfigModel;
import ch.bfh.model.SupportedBrowsers;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ConfigFileHelper {
    private static final String configFilePath = "src/main/resources/config.json";
    public static ConfigModel read() throws ConfigFileException {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        File configFile = new File(configFilePath);
        ConfigFileMapperModel configMapper = new ConfigFileMapperModel();
        ConfigModel config = new ConfigModel();

        if (configFile.exists() && !configFile.isDirectory()) {
            try {
                configMapper = objectMapper.readValue(configFile, ConfigFileMapperModel.class);
            } catch (IOException e) {
                throw(new ConfigFileException(e.getMessage()));
            }
        }

        config.setAccessKey(configMapper.getAccessKey());
        config.setSecretKey(configMapper.getSecretKey());

        switch(configMapper.getBrowser().toUpperCase()) {
            case "FIREFOX":
                config.setBrowser(SupportedBrowsers.FIREFOX);
                break;
            case "EDGE":
                config.setBrowser(SupportedBrowsers.EDGE);
                break;
            case "CHROME":
                config.setBrowser(SupportedBrowsers.CHROME);
                break;
            case "", "DEFAULT":
                config.setBrowser(SupportedBrowsers.DEFAULT);
                break;
            default:
                config.setBrowser(SupportedBrowsers.UNSUPPORTED);
        }

        return config;
    }

    public static void save(ConfigModel config) throws ConfigFileException {
        ObjectMapper objectMapper = new ObjectMapper();
        ConfigFileMapperModel configMapper = new ConfigFileMapperModel();
        configMapper.setAccessKey(config.getAccessKey());
        configMapper.setSecretKey(config.getSecretKey());
        configMapper.setBrowser(config.getBrowser().name());

        try {
            objectMapper.writeValue(new File(configFilePath), configMapper);
        } catch (IOException e) {
            throw(new ConfigFileException(e.getMessage()));
        }
    }

    public static SupportedBrowsers getBrowser() throws ConfigFileException {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        File configFile = new File(configFilePath);
        ConfigFileMapperModel configMapper = new ConfigFileMapperModel();

        if (configFile.exists() && !configFile.isDirectory()) {
            try {
                configMapper = objectMapper.readValue(configFile, ConfigFileMapperModel.class);
            } catch (IOException e) {
                throw(new ConfigFileException(e.getMessage()));
            }
        }

        switch(configMapper.getBrowser().toUpperCase()) {
            case "FIREFOX":
                return SupportedBrowsers.FIREFOX;
            case "EDGE":
                return SupportedBrowsers.EDGE;
            case "CHROME":
                return SupportedBrowsers.CHROME;
            case "", "DEFAULT":
                return SupportedBrowsers.DEFAULT;
            default:
                return SupportedBrowsers.UNSUPPORTED;
        }

    }

}
