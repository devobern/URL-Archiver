package ch.bfh.archiver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.InetAddress;
import java.time.Duration;
import java.util.List;

/**
 * Implementation of the URLArchiver interface for the Archive.today service.
 * This class provides the mechanism to archive URLs using Archive.today's archiving capabilities.
 */
public class ArchiveTodayArchiver implements URLArchiver {

    private final String serviceName = "ArchiveToday";
    private final String serviceUrl = "https://archive.today";
    private final String hostName = "archive.today";

    /**
     * Archives the given URL using the Archive.today service.
     *
     * @param url The URL to be archived.
     * @return A string representing the archived URL, or null if the service is not available.
     */
    @Override
    public String archiveURL(String url) {
        // Instantiate a ChromeDriver class
        WebDriver driver = new ChromeDriver();

        String archivedUrl = null;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(300));

        try {
            // Launch Website
            driver.navigate().to(serviceUrl);

            // Wait for the input field to be visible and enter the URL
            WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("url")));
            inputField.sendKeys(url);

            // Find and click the archive button
            WebElement archiveButton = driver.findElement(By.xpath("//input[@type='submit'][@value='speichern']"));
            archiveButton.click();

            // Wait for the CAPTCHA to be present
            WebElement captcha = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("g-recaptcha")));
            // Inform the user that they need to solve the CAPTCHA manually
            System.out.println("Please solve the CAPTCHA in the browser.");
            wait.until(ExpectedConditions.invisibilityOf(captcha));
            System.out.println("CAPTCHA solved.");

            // Check if the "DIVALREADY" element is present which means the site was already archived
            List<WebElement> divAlreadyList = driver.findElements(By.id("DIVALREADY"));

            if (!divAlreadyList.isEmpty()) {
                // If "DIVALREADY" is present, click the "save" button
                WebElement saveButton = driver.findElement(By.xpath("//input[@type='submit'][@value='save']"));
                saveButton.click();
            }

            System.out.println("Wait for the 'DIVSHARE'");
            // Wait for the 'DIVSHARE' element to be present
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("DIVSHARE")));

            archivedUrl = driver.getCurrentUrl();
            System.out.println("Archived URL: " + archivedUrl);

        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
        finally {
            // Close the Browser
            driver.quit();
        }
        return archivedUrl;
    }

    /**
     * Checks whether the Archive.today service is available for use.
     *
     * @return true if the service is available, false otherwise.
     */
    @Override
    public boolean isAvailable() {
        try {
            InetAddress address = InetAddress.getByName(hostName);
            return address.isReachable(5000);
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
        return serviceName;
    }
}
