package ch.bfh.helper;

import ch.bfh.exceptions.ArchiverException;
import ch.bfh.exceptions.ConfigFileException;
import ch.bfh.model.SupportedBrowsers;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

/**
 * Factory class for creating {@link WebDriver} instances tailored for different browsers.
 * Supports creation of web drivers for browsers like Edge, Chrome, and Firefox,
 * with consideration for the underlying operating system.
 */
public class WebDriverFactory {

    /**
     * Generates browser-specific options with a common {@link PageLoadStrategy}.
     *
     * @param browser the browser for which to create options
     * @return configured {@link MutableCapabilities} for the specified browser
     * @throws IllegalArgumentException if the browser is unsupported
     */
    private static MutableCapabilities getBrowserOptions(SupportedBrowsers browser) {
        AbstractDriverOptions<?> options = switch (browser) {
            case FIREFOX -> new FirefoxOptions();
            case CHROME -> new ChromeOptions();
            case EDGE -> new EdgeOptions();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    /**
     * Determines the operating system of the current environment.
     *
     * @return the detected {@link OperatingSystem}
     */
    private static OperatingSystem getOperatingSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        return switch (os) {
            case String s when s.contains("win") -> OperatingSystem.WINDOWS;
            case String s when s.contains("mac") -> OperatingSystem.MACOS;
            case String s when s.contains("nix") || s.contains("nux") || s.contains("aix") -> OperatingSystem.LINUX;
            default -> OperatingSystem.UNKNOWN;
        };
    }

    /**
     * Provides a default WebDriver instance based on the current operating system.
     *
     * @return An instance of {@link WebDriver} suitable for the detected OS.
     * @throws ArchiverException if the operating system is macOS or an unknown type
     */
    private static WebDriver getDefaultDriver() throws ArchiverException {
        OperatingSystem currentOS = getOperatingSystem();
        return switch (currentOS) {
            case WINDOWS -> new EdgeDriver((EdgeOptions) getBrowserOptions(SupportedBrowsers.EDGE));
            case LINUX -> new FirefoxDriver((FirefoxOptions) getBrowserOptions(SupportedBrowsers.FIREFOX));
            case MACOS -> throw new ArchiverException(I18n.getString("action.archiving.unsupportedBrowser.macDefault"));
            default -> new ChromeDriver((ChromeOptions) getBrowserOptions(SupportedBrowsers.CHROME));
        };
    }

    /**
     * Creates a WebDriver instance based on the configured browser preference.
     * Defaults to the system's preferred browser if the configuration is unavailable or invalid.
     *
     * @return An instance of {@link WebDriver} based on browser configuration or system preference.
     * @throws ArchiverException if an unsupported browser is specified
     */
    public static WebDriver getWebDriver() throws ArchiverException {
        SupportedBrowsers browser;
        try {
            browser = ConfigFileHelper.getBrowser();
        } catch (ConfigFileException e) {
            browser = SupportedBrowsers.DEFAULT;
        }

        if (browser == SupportedBrowsers.UNSUPPORTED) {
            throw new ArchiverException(I18n.getString("action.archiving.unsupportedBrowser.error"));
        }

        try {
            return switch (browser) {
                case FIREFOX -> new FirefoxDriver((FirefoxOptions) getBrowserOptions(browser));
                case CHROME -> new ChromeDriver((ChromeOptions) getBrowserOptions(browser));
                case EDGE -> new EdgeDriver((EdgeOptions) getBrowserOptions(browser));
                case DEFAULT -> getDefaultDriver();
                default -> throw new ArchiverException(I18n.getString("action.archiving.unsupportedBrowser.error"));
            };
        } catch (IllegalArgumentException e) {
            // Handle case where the browser is not supported
            throw new ArchiverException(I18n.getString("action.archiving.unsupportedBrowser.error"), e);
        }
    }
}
