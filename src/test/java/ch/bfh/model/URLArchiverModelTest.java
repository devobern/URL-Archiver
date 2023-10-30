package ch.bfh.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link URLArchiverModel} class.
 */
public class URLArchiverModelTest {

    /** The model instance under test. */
    private URLArchiverModel model;

    /**
     * Sets up the testing environment before each test. Initializes a new model instance.
     */
    @BeforeEach
    public void setUp() {
        model = new URLArchiverModel();
    }

    /**
     * Test method for {@link URLArchiverModel#addExtractedURL(String, int)}.
     * Validates that an extracted URL is added correctly and associated with the right line number.
     */
    @Test
    public void testAddExtractedURL() {
        String extractedURL = "http://example.com";
        int lineNumber = 5;
        model.addExtractedURL(extractedURL, lineNumber);

        List<URLPair> urlPairs = model.getUrlPairs();
        assertEquals(1, urlPairs.size());

        URLPair pair = urlPairs.get(0);
        assertEquals(extractedURL, pair.getExtractedURL());
        assertEquals(lineNumber, pair.getLineNumber());
    }

    /**
     * Test method for {@link URLArchiverModel#setArchivedURL(String, String)}.
     * Validates that an archived URL is set correctly for an extracted URL.
     */
    @Test
    public void testSetArchivedURL() {
        String extractedURL = "http://example.com";
        String archivedURL = "http://archive.org/example";
        model.addExtractedURL(extractedURL, 5);
        model.setArchivedURL(extractedURL, archivedURL);

        List<URLPair> urlPairs = model.getUrlPairs();
        assertEquals(1, urlPairs.size());

        URLPair pair = urlPairs.get(0);
        assertEquals(archivedURL, pair.getArchivedURL());
    }

    /**
     * Test method for {@link URLArchiverModel#setUrlPairs(List)} and {@link URLArchiverModel#getUrlPairs()}.
     * Validates the correct retrieval of set URL pairs.
     */
    @Test
    public void testSetAndGetUrlPairs() {
        List<URLPair> pairs = new ArrayList<>();
        pairs.add(new URLPair("http://example1.com", 1));
        pairs.add(new URLPair("http://example2.com", 2));

        model.setUrlPairs(pairs);

        List<URLPair> retrievedPairs = model.getUrlPairs();
        assertEquals(2, retrievedPairs.size());
        assertTrue(retrievedPairs.containsAll(pairs));
    }
}
