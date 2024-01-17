package ch.bfh.model;

/**
 * Represents the configuration settings for the application.
 */
public class ConfigModel {
    private String accessKey;
    private String secretKey;
    private SupportedBrowsers browser;

    /**
     * Constructs a ConfigModel with default values.
     */
    public ConfigModel() {
        this("", "", SupportedBrowsers.DEFAULT);
    }

    /**
     * Constructs a ConfigModel with specified access and secret keys.
     * Uses the default browser setting.
     *
     * @param accessKey The access key to be used.
     * @param secretKey The secret key to be used.
     */
    public ConfigModel(String accessKey, String secretKey) {
        this(accessKey, secretKey, SupportedBrowsers.DEFAULT);
    }

    /**
     * Constructs a ConfigModel with specified access key, secret key, and browser preference.
     *
     * @param accessKey The access key to be used.
     * @param secretKey The secret key to be used.
     * @param browser   The browser preference.
     */
    public ConfigModel(String accessKey, String secretKey, SupportedBrowsers browser) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.browser = browser;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public SupportedBrowsers getBrowser() {
        return browser;
    }

    public void setBrowser(SupportedBrowsers browser) {
        this.browser = browser;
    }
}
