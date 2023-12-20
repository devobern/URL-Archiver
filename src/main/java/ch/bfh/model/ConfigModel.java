package ch.bfh.model;

/**
 * Represents the configuration settings for the application.
 * This includes access key, secret key, and browser preference.
 */
public class ConfigModel {
    private String accessKey;
    private String secretKey;
    private SupportedBrowsers browser;

    /**
     * Default constructor initializing with default values.
     */
    public ConfigModel() {
        this.accessKey = "";
        this.secretKey = "";
        this.browser = SupportedBrowsers.DEFAULT;
    }

    /**
     * Constructor to initialize with specific access and secret keys.
     * The browser is set to the default.
     *
     * @param accessKey The access key to be used.
     * @param secretKey The secret key to be used.
     */
    public ConfigModel(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.browser = SupportedBrowsers.DEFAULT;
    }

    /**
     * Constructor to initialize with specific access key, secret key, and browser type.
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
