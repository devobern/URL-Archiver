package ch.bfh.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link URLPair} class.
 */
public class URLPairTest {

    /**
     * Test method for {@link URLPair#getExtractedURL()}.
     * Validates that the correct extracted URL is retrieved from the URLPair instance.
     */
    @Test
    public void testGetExtractedURL() {
        URLPair pair = new URLPair("http://example.com");
        assertEquals("http://example.com", pair.getExtractedURL());
    }

    /**
     * Test method for {@link URLPair#getArchivedURLs()}.
     * Validates that the archived URL is initially null.
     */
    @Test
    public void testGetArchivedURL_InitiallyNull() {
        URLPair pair = new URLPair("http://example.com");
        assertTrue(pair.getArchivedURLs().isEmpty());
    }

    /**
     * Test methods for {@link URLPair#setArchivedURLs(List<String>)} and {@link URLPair#getArchivedURLs()}.
     * Validates that an archived URL can be set and subsequently retrieved.
     */
    @Test
    public void testGetAndSetArchivedURL() {
        URLPair pair = new URLPair("http://example.com");
        List<String> archivedURLs = new ArrayList<>();
        archivedURLs.add("http://archive.org/example");
        pair.setArchivedURLs(archivedURLs);
        assertEquals("http://archive.org/example", pair.getArchivedURLs().getFirst());
    }

    /**
     * Test method for {@link URLPair#getLineNumber()}.
     * Validates that the correct line number is retrieved from the URLPair instance.
     */
    @Test
    public void testGetLineNumber() {
        URLPair pair = new URLPair("http://example.com");
        assertEquals(0, pair.getLineNumber());
    }

    /**
     * Test method for {@link URLPair#toString()}.
     * Validates the string representation of the URLPair instance.
     */
    @Test
    public void testToString() {
        URLPair pair = new URLPair("http://example.com");
        List<String> archivedURLs = new ArrayList<>();
        archivedURLs.add("http://archive.org/example");
        pair.setArchivedURLs(archivedURLs);
        String expectedString = "URLPair { Extracted URL: http://example.com, Archived URL: [http://archive.org/example], Line: 0 }";
        assertEquals(expectedString, pair.toString());
    }
}
