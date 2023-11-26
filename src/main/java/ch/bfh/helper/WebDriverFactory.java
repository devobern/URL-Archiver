package ch.bfh.helper;

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

public class WebDriverFactory {

    private static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    private static SafariOptions getSafariOptions() {
        SafariOptions options = new SafariOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }
    private static String getOperatingSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        return switch (os) {
            case String s when s.contains("win") -> "Windows";
            case String s when s.contains("mac") -> "macOS";
            case String s when s.contains("nix") || os.contains("nux") || os.contains("aix") -> "Linux";
            default -> "Unknown";
        };
    }

    public static WebDriver getWebDriver() {
        String currentOS = getOperatingSystem();
        return switch (currentOS) {
            case "Windows" -> new EdgeDriver(getEdgeOptions());
            case "macOS" -> new SafariDriver(getSafariOptions());
            case "Linux" -> new FirefoxDriver(getFirefoxOptions());
            default -> new ChromeDriver(getChromeOptions());
        };
    }

}
