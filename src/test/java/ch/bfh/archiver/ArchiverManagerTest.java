package ch.bfh.archiver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArchiverManagerTest {
    private ArchiverManager manager;
    private URLArchiver waybackArchiver;
    private URLArchiver archiveTodayArchiver;

    // Set up method that runs before each test.
    // It initializes the ArchiverManager and mock URLArchiver instances.
    @BeforeEach
    void setUp() {
        // Create a new ArchiverManager instance.
        manager = new ArchiverManager();
        // Create mock implementations for the URLArchiver interface.
        waybackArchiver = Mockito.mock(URLArchiver.class);
        archiveTodayArchiver = Mockito.mock(URLArchiver.class);

        // Configure the mocks to return specific service names when getServiceName is called.
        Mockito.when(waybackArchiver.getServiceName()).thenReturn("WaybackMachine");
        Mockito.when(archiveTodayArchiver.getServiceName()).thenReturn("ArchiveToday");

        // Add the mock archivers to the manager.
        manager.addArchiver(waybackArchiver);
        manager.addArchiver(archiveTodayArchiver);
    }

    // Test archiving when both services are available.
    // It should result in URLs being archived and no services reported as unavailable.
    @Test
    void whenArchiveCalledWithAvailableServices_thenArchiveUrls() {
        String testUrl = "http://example.com";
        // Set the mocks to simulate available services.
        Mockito.when(waybackArchiver.isAvailable()).thenReturn(true);
        Mockito.when(archiveTodayArchiver.isAvailable()).thenReturn(true);
        // Configure the mocks to return specific archived URLs.
        Mockito.when(waybackArchiver.archiveURL(testUrl)).thenReturn("http://archived.example.com/wayback");
        Mockito.when(archiveTodayArchiver.archiveURL(testUrl)).thenReturn("http://archived.example.com/archivetoday");

        // Call the archive method and capture the result.
        ArchiverResult result = manager.archive(testUrl, List.of(waybackArchiver, archiveTodayArchiver));

        // Assert that the archived URLs list contains the expected URLs.
        assertTrue(result.getArchivedUrls().contains("http://archived.example.com/wayback"));
        assertTrue(result.getArchivedUrls().contains("http://archived.example.com/archivetoday"));
        // Assert that no services are reported as unavailable.
        assertTrue(result.getUnavailableArchivers().isEmpty());
    }

    // Test archiving when both services are unavailable.
    // It should result in no URLs being archived and both services reported as unavailable.
    @Test
    void whenArchiveCalledWithUnavailableServices_thenReportUnavailable() {
        String testUrl = "http://example.com";
        // Set the mocks to simulate unavailable services.
        Mockito.when(waybackArchiver.isAvailable()).thenReturn(false);
        Mockito.when(archiveTodayArchiver.isAvailable()).thenReturn(false);

        // Call the archive method and capture the result.
        ArchiverResult result = manager.archive(testUrl, List.of(waybackArchiver, archiveTodayArchiver));

        // Assert that the archived URLs list is empty.
        assertTrue(result.getArchivedUrls().isEmpty());
        // Assert that both services are reported as unavailable.
        assertTrue(result.getUnavailableArchivers().contains("WaybackMachine"));
        assertTrue(result.getUnavailableArchivers().contains("ArchiveToday"));
    }

    // Test archiving when one service is available and the other is not.
    // It should result in one URL being archived and the unavailable service reported as such.
    @Test
    void whenArchiveCalledWithPartialUnavailableServices_thenArchiveUrlsAndReportUnavailable() {
        String testUrl = "http://example.com";
        // Set one service to be unavailable and the other to be available.
        Mockito.when(waybackArchiver.isAvailable()).thenReturn(false);
        Mockito.when(archiveTodayArchiver.isAvailable()).thenReturn(true);
        // Configure the available service to return a specific archived URL.
        Mockito.when(archiveTodayArchiver.archiveURL(testUrl)).thenReturn("http://archived.example.com/archivetoday");

        // Call the archive method and capture the result.
        ArchiverResult result = manager.archive(testUrl, List.of(waybackArchiver, archiveTodayArchiver));

        // Assert that there is one archived URL in the list.
        assertEquals(1, result.getArchivedUrls().size());
        // Assert that the archived URL is the one returned by the available service.
        assertTrue(result.getArchivedUrls().contains("http://archived.example.com/archivetoday"));
        // Assert that the unavailable service is reported as such.
        assertTrue(result.getUnavailableArchivers().contains("WaybackMachine"));
        // Assert that the available service is not reported as unavailable.
        assertFalse(result.getUnavailableArchivers().contains("ArchiveToday"));
    }

}
