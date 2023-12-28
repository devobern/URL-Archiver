package ch.bfh.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        URLPair pair = new URLPair("https://example.com");
        assertEquals("https://example.com", pair.getExtractedURL(), "Extracted URL should match the one set in constructor");
    }

    /**
     * Test method for {@link URLPair#getArchivedURLs()}.
     * Validates that the list of archived URLs is initially empty.
     */
    @Test
    public void testGetArchivedURL_InitiallyEmpty() {
        URLPair pair = new URLPair("https://example.com");
        assertTrue(pair.getArchivedURLs().isEmpty(), "Archived URLs should be initially empty");
    }

    /**
     * Test methods for {@link URLPair#setArchivedURLs(List<String>)} and {@link URLPair#getArchivedURLs()}.
     * Validates that archived URLs can be set and subsequently retrieved.
     */
    @Test
    public void testSetAndGetArchivedURLs() {
        URLPair pair = new URLPair("https://example.com");
        List<String> archivedURLs = new ArrayList<>();
        archivedURLs.add("https://archive.org/example");
        pair.setArchivedURLs(archivedURLs);
        assertEquals(archivedURLs, pair.getArchivedURLs(), "Archived URLs should match those set with setArchivedURLs");
    }

    /**
     * Test method for {@link URLPair#toString()}.
     * Validates the string representation of the URLPair instance.
     */
    @Test
    public void testToString() {
        URLPair pair = new URLPair("https://example.com");
        List<String> archivedURLs = new ArrayList<>();
        archivedURLs.add("https://archive.org/example");
        pair.setArchivedURLs(archivedURLs);
        String expectedString = "URLPair { Extracted URL: https://example.com, Archived URLs: [https://archive.org/example] }";
        assertEquals(expectedString, pair.toString(), "String representation should match the expected format");
    }
}
