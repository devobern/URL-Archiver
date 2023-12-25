package ch.bfh.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test suite for the FileModel class.
 */
class FileModelTest {

    private final String testMimeType = "text/plain";
    @TempDir
    Path tempDir;
    private FileModel fileModel;
    private Path testPath;

    @BeforeEach
    void setUp() {
        testPath = tempDir.resolve("test.txt");
        fileModel = new FileModel(testPath, testMimeType);
    }

    /**
     * Test if the FileModel object is constructed with the correct file name.
     */
    @Test
    void testGetFileName() {
        assertEquals("test.txt", fileModel.getFileName(),
                "File name should match the name in the path");
    }

    /**
     * Test if the FileModel object is constructed with the correct MIME type.
     */
    @Test
    void testGetMimeType() {
        assertEquals(testMimeType, fileModel.getMimeType(),
                "MIME type should match the provided MIME type");
    }

    /**
     * Test if the FileModel object is constructed with the correct file path.
     */
    @Test
    void testGetFilePath() {
        assertEquals(testPath, fileModel.getFilePath(),
                "File path should match the provided file path");
    }

    /**
     * Test adding extracted URLs and retrieving them.
     */
    @Test
    void testAddExtractedURLsAndGetUrlPairs() {
        Set<String> extractedURLs = new HashSet<>(Arrays.asList("https://example.com", "https://test.com"));
        fileModel.addExtractedURLs(extractedURLs);

        List<URLPair> urlPairs = fileModel.getUrlPairs();
        assertEquals(2, urlPairs.size(),
                "Number of URL pairs should be equal to the number of extracted URLs");
        assertTrue(urlPairs.stream().anyMatch(pair -> pair.getExtractedURL().equals("https://example.com")),
                "List should contain the URL 'https://example.com'");
        assertTrue(urlPairs.stream().anyMatch(pair -> Objects.equals(pair.getExtractedURL(), "https://test.com")),
                "List should contain the URL 'https://test.com'");
    }

    /**
     * Test setting archived URLs for a specific extracted URL.
     */
    @Test
    void testSetArchivedURL() {
        String extractedURL = "https://example.com";
        List<String> archivedURLs = Arrays.asList("https://archive1.com", "https://archive2.com");
        fileModel.addExtractedURLs(new HashSet<>(Collections.singletonList(extractedURL)));
        fileModel.setArchivedURL(extractedURL, archivedURLs);

        URLPair pair = fileModel.getUrlPairs().getFirst();
        assertEquals(archivedURLs, pair.getArchivedURLs(),
                "Archived URLs should match the provided archived URLs");
    }

    /**
     * Test adding an archived URL to a specific extracted URL.
     */
    @Test
    void testAddArchivedURL() {
        String extractedURL = "https://example.com";
        String archivedURL = "https://archive.com";
        fileModel.addExtractedURLs(new HashSet<>(Collections.singletonList(extractedURL)));
        fileModel.addArchivedURL(extractedURL, archivedURL);

        URLPair pair = fileModel.getUrlPairs().getFirst();
        assertTrue(pair.getArchivedURLs().contains(archivedURL),
                "The archived URL should be added to the URL pair");
    }
}
