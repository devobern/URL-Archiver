package ch.bfh.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        URLPair pair = new URLPair("http://example.com", 5);
        assertEquals("http://example.com", pair.getExtractedURL());
    }

    /**
     * Test method for {@link URLPair#getArchivedURL()}.
     * Validates that the archived URL is initially null.
     */
    @Test
    public void testGetArchivedURL_InitiallyNull() {
        URLPair pair = new URLPair("http://example.com", 5);
        assertNull(pair.getArchivedURL());
    }

    /**
     * Test methods for {@link URLPair#setArchivedURL(String)} and {@link URLPair#getArchivedURL()}.
     * Validates that an archived URL can be set and subsequently retrieved.
     */
    @Test
    public void testGetAndSetArchivedURL() {
        URLPair pair = new URLPair("http://example.com", 5);
        pair.setArchivedURL("http://archive.org/example");
        assertEquals("http://archive.org/example", pair.getArchivedURL());
    }

    /**
     * Test method for {@link URLPair#getLineNumber()}.
     * Validates that the correct line number is retrieved from the URLPair instance.
     */
    @Test
    public void testGetLineNumber() {
        URLPair pair = new URLPair("http://example.com", 5);
        assertEquals(5, pair.getLineNumber());
    }

    /**
     * Test method for {@link URLPair#toString()}.
     * Validates the string representation of the URLPair instance.
     */
    @Test
    public void testToString() {
        URLPair pair = new URLPair("http://example.com", 5);
        pair.setArchivedURL("http://archive.org/example");
        String expectedString = "URLPair { Extracted URL: http://example.com, Archived URL: http://archive.org/example, Line: 5 }";
        assertEquals(expectedString, pair.toString());
    }
}
