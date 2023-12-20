package ch.bfh.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents the model for configuration file mapping.
 * This class is used to map the JSON configuration data to Java objects.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigFileMapperModel {
    private final String accessKey;
    private final String secretKey;
    private final String browser;

    /**
     * Default constructor initializes the model with empty strings.
     */
    public ConfigFileMapperModel() {
        this("", "", "");
    }

    /**
     * Constructs a new ConfigFileMapperModel with specified values.
     *
     * @param accessKey the access key
     * @param secretKey the secret key
     * @param browser   the browser type
     */
    public ConfigFileMapperModel(String accessKey, String secretKey, String browser) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.browser = browser;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBrowser() {
        return browser;
    }
}
