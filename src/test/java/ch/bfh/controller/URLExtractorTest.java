package ch.bfh.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

/**
 * Unit tests for the {@link URLExtractor} class.
 */
class URLExtractorTest {

    @Test
    void extractURLsWithValidURLs() {
        // Test to verify that valid URLs are correctly extracted from a text.
        String text = "Visit https://example.com and http://test.com.";
        Set<String> extractedURLs = URLExtractor.extractURLs(text);

        assertAll("Valid URLs",
                () -> assertTrue(extractedURLs.contains("https://example.com"), "Should contain https://example.com"),
                () -> assertTrue(extractedURLs.contains("http://test.com"), "Should contain http://test.com")
        );
    }

    @Test
    void extractURLsWhenNoURLsPresent() {
        // Test to ensure that no URLs are extracted from text without URLs.
        String text = "This is a sample text without any URLs.";
        Set<String> extractedURLs = URLExtractor.extractURLs(text);

        assertTrue(extractedURLs.isEmpty(), "Set should be empty when no URLs are present");
    }

    @Test
    void extractURLsShouldIgnoreDuplicates() {
        // Test to confirm that duplicate URLs are identified and only one instance is kept.
        String text = "Duplicate URL http://example.com and http://example.com.";
        Set<String> extractedURLs = URLExtractor.extractURLs(text);

        assertEquals(1, extractedURLs.size(), "Duplicate URLs should be counted once");
        assertTrue(extractedURLs.contains("http://example.com"), "Should contain http://example.com");
    }

    @Test
    void extractURLsWithNullInput() {
        // Test to verify that the method throws a NullPointerException when null is passed as input.
        assertThrows(NullPointerException.class,
                () -> URLExtractor.extractURLs(null),
                "Extracting URLs from null should throw NullPointerException");
    }

    @Test
    void extractComplexURLs() {
        // Test to check the extraction of URLs with complex structures like paths, subdomains, and query parameters.
        String text = "Text with URLs: https://example.com/path, http://subdomain.example.net, and https://www.test.com:8080/query?name=value#anchor";
        Set<String> extractedURLs = URLExtractor.extractURLs(text);

        assertAll("Complex URLs",
                () -> assertTrue(extractedURLs.contains("https://example.com/path")),
                () -> assertTrue(extractedURLs.contains("http://subdomain.example.net")),
                () -> assertTrue(extractedURLs.contains("https://www.test.com:8080/query?name=value#anchor"))
        );
    }

    @Test
    void extractURLsWithSpecialCharacters() {
        // Test to ensure URLs with special characters like query parameters and spaces (encoded as %20) are extracted correctly.
        String text = "Check out https://example.com?query=value&anotherParam=anotherValue and https://example.com/path/name%20with%20spaces";
        Set<String> extractedURLs = URLExtractor.extractURLs(text);

        assertAll("URLs with Special Characters",
                () -> assertTrue(extractedURLs.contains("https://example.com?query=value&anotherParam=anotherValue")),
                () -> assertTrue(extractedURLs.contains("https://example.com/path/name%20with%20spaces"))
        );
    }

    @Test
    void extractURLsEmbeddedInText() {
        // Test to evaluate the extraction of URLs from within a complex text, including ignoring invalid URLs.
        String text = "This is a tricky one, visit https://example.com and http://not.a.real.url, also check out www.test.com (which is not a valid URL in this context).";
        Set<String> extractedURLs = URLExtractor.extractURLs(text);

        assertTrue(extractedURLs.contains("https://example.com"), "Should correctly extract valid URLs embedded in complex text");
        assertTrue(extractedURLs.contains("http://not.a.real.url"), "Should correctly extract invalid URLs");
        assertFalse(extractedURLs.contains("www.test.com"), "Should ignore URLs without a protocol");
    }

    @Test
    void extractURLsWithAdjacentText() {
        // Test to check if URLs are correctly extracted even when they are adjacent to other text without spaces.
        String text = "Adjacent text can cause issues, like in this URL:https://example.com.";
        Set<String> extractedURLs = URLExtractor.extractURLs(text);

        assertTrue(extractedURLs.contains("https://example.com"), "Should extract URL even when adjacent to other text");
    }

}
