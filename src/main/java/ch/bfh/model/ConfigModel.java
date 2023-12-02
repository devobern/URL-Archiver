package ch.bfh.model;

public class ConfigModel {
    private String accessKey;
    private String secretKey;
    private SupportedBrowsers browser;

    public ConfigModel() {
        this.accessKey = "";
        this.secretKey = "";
        this.browser = SupportedBrowsers.DEFAULT;
    }
    public ConfigModel(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.browser = SupportedBrowsers.DEFAULT;
    }

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
