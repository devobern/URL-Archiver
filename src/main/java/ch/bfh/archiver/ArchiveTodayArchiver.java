package ch.bfh.archiver;

import ch.bfh.exceptions.ArchiverException;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.http.ConnectionFailedException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.*;
import java.time.Duration;
import java.util.List;

import static ch.bfh.helper.WebDriverFactory.getWebDriver;

// Todo: If the browser gets manually closed by the user, there is a big nasty error. Can we catch this case?

/**
 * Implementation of the URLArchiver interface for the Archive.today service.
 * This class provides the mechanism to archive URLs using Archive.today's archiving capabilities.
 */
public class ArchiveTodayArchiver implements URLArchiver {
    private static final String SERVICE_NAME = "ArchiveToday";
    private static final String SERVICE_URL = "https://archive.today";
    private static final int TIMEOUT_SECONDS = 300;
    private static final int POLLING_INTERVAL_MS = 300;
    private static final String HOST_NAME = "archive.today";
    private static final int PORT = 80;
    private static final int SOCKET_TIMEOUT_MS = 5000;

    /**
     * Archives the given URL using the Archive.today service.
     *
     * @param url The URL to be archived.
     * @return A string representing the archived URL, or null if the service is not available.
     */
    @Override
    public String archiveURL(String url) throws ArchiverException {
        // Todo: The url cannot be null or empty at this point. But double check is also ok.
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        WebDriver driver = getWebDriver();
        String archivedUrl;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));

        try {
            // Launch Website
            driver.navigate().to(SERVICE_URL);

            // Wait for the input field to be visible and enter the URL
            WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("url")));
            inputField.sendKeys(url);

            // Find and click the archive button
            WebElement archiveButton = driver.findElement(By.xpath("//form[@id='submiturl']//input[@type='submit']"));
            archiveButton.click();

            // Wait for the CAPTCHA to be present
            WebElement captcha = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("g-recaptcha")));
            wait
                    .pollingEvery(Duration.ofMillis(POLLING_INTERVAL_MS))
                    .until(ExpectedConditions.invisibilityOf(captcha));

            // Check if the "DIVALREADY" element is present which means the site was already archived
            List<WebElement> divAlreadyList = driver.findElements(By.id("DIVALREADY"));

            if (!divAlreadyList.isEmpty()) {
                // If "DIVALREADY" is present, click the "save" button
                WebElement saveButton = driver.findElement(By.xpath("//input[@type='submit'][@value='save']"));
                saveButton.click();
            }

            // Wait for the 'DIVSHARE' element to be present
            wait
                    .pollingEvery(Duration.ofMillis(POLLING_INTERVAL_MS))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("DIVSHARE")));

            // Get archived site URL
            archivedUrl = driver.getCurrentUrl();
        } catch (TimeoutException e) {
           throw new ArchiverException("The URL could not be archived in less than five minutes!");
        } catch (ConnectionFailedException e){
           throw new ArchiverException("The browser was closed or the network connection was closed!");
        }
        finally {
            // Close the Browser
            driver.quit();
        }
        return archivedUrl;
    }

    /**
     * Checks if the host is reachable within a 5-second timeout.
     * This method establishes a TCP connection on a known open port to determine reachability,
     * offering more reliable results than standard ICMP methods, especially on Unix systems.
     *
     * @return true if reachable, false otherwise.
     */
    public boolean isAvailable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(HOST_NAME, PORT), SOCKET_TIMEOUT_MS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Returns the name of the archiving service.
     *
     * @return A string representing the name of the service.
     */
    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }
}
