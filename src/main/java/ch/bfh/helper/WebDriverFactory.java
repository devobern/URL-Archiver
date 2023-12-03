package ch.bfh.helper;

import ch.bfh.exceptions.ArchiverException;
import ch.bfh.exceptions.ConfigFileException;
import ch.bfh.model.ConfigModel;
import ch.bfh.model.SupportedBrowsers;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

/**
 * A factory class for creating instances of {@link WebDriver} for different browsers.
 * This class provides a centralized way to create and configure web drivers for
 * Edge, Safari, Firefox, and Chrome. Based on the operating system an appropriate
 * WebDriver instance with a common page load strategy is returned.
 * Windows, macOS, and Linux are supported operating systems.
 */
public class WebDriverFactory {

    /**
     * Creates and returns EdgeOptions with the EAGER {@link PageLoadStrategy}.
     *
     * @return {@link EdgeOptions} configured for the WebDriver.
     */
    private static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }


    /**
     * Creates and returns ChromeOptions with the EAGER {@link PageLoadStrategy}.
     *
     * @return {@link ChromeOptions} configured for the WebDriver.
     */
    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    /**
     * Creates and returns FirefoxOptions with the EAGER {@link PageLoadStrategy}.
     *
     * @return {@link FirefoxOptions} configured for the WebDriver.
     */
    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    /**
     * Determines the operating system of the current environment.
     *
     * @return A string representing the operating system name, e.g., "Windows", "macOS", "Linux".
     */
    private static String getOperatingSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        return switch (os) {
            case String s when s.contains("win") -> "Windows";
            case String s when s.contains("mac") -> "macOS";
            case String s when s.contains("nix") || os.contains("nux") || os.contains("aix") -> "Linux";
            default -> "Unknown";
        };
    }

    /**
     * Provides a WebDriver instance based on the current operating system.
     * It returns an EdgeDriver for Windows, SafariDriver for macOS, FirefoxDriver for Linux,
     * and ChromeDriver for other or unknown operating systems.
     *
     * @return An instance of {@link WebDriver} appropriate for the current operating system.
     */
    public static WebDriver getWebDriver() throws ArchiverException {
        String currentOS = getOperatingSystem();
        SupportedBrowsers browser;
        try {
            browser = ConfigFileHelper.getBrowser();
        } catch (ConfigFileException e) {
            browser = SupportedBrowsers.DEFAULT;
        }

        if (browser.equals(SupportedBrowsers.UNSUPPORTED)) {
            throw(new ArchiverException(I18n.getString("action.archiving.unsupportedBrowser.error")));
        }
        switch (browser) {
            case FIREFOX -> {
                return new FirefoxDriver(getFirefoxOptions());
            }
            case CHROME -> {
                return new ChromeDriver(getChromeOptions());
            }
            case EDGE -> {
                return new EdgeDriver(getEdgeOptions());
            }
            case DEFAULT -> {
                return switch (currentOS) {
                    case "Windows" -> new EdgeDriver(getEdgeOptions());
                    case "macOS" -> throw(new ArchiverException(I18n.getString("action.archiving.unsupportedBrowser.macDefault")));
                    case "Linux" -> new FirefoxDriver(getFirefoxOptions());
                    default -> new ChromeDriver(getChromeOptions());
                };
            }
            default -> {
                throw(new ArchiverException(I18n.getString("action.archiving.unsupportedBrowser.error")));
            }

        }

    }

}
