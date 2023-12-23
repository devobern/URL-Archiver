package ch.bfh.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link I18n} helper class.
 */
class I18nTest {

    /**
     * Setup logic executed before each test. Ensures fresh loading
     * of the ResourceBundle for each test case.
     */
    @BeforeEach
    void setup() {
        // Reset the ResourceBundle to null before each test to ensure
        // fresh loading of the bundle for each test case.
        I18n.getResourceBundle(Locale.ENGLISH);
    }

    /**
     * Test method for {@link I18n#getResourceBundle(Locale)}.
     * Validates that the correct resource bundle is loaded for a given locale.
     */
    @Test
    void testGetResourceBundle() {
        // Testing for English locale
        Locale locale = Locale.ENGLISH;
        assertEquals(locale, I18n.getResourceBundle(locale).getLocale());
    }

    /**
     * Test method for {@link I18n#getString(String)}.
     * Validates the value returned for a valid key.
     */
    @Test
    void testGetString_ValidKey() {
        String key = "path.prompt";
        String expectedValue = "Enter the file/directory path:";
        assertEquals(expectedValue, I18n.getString(key));
    }

    /**
     * Test method for {@link I18n#getString(String)}.
     * Validates the value returned when an invalid key is provided.
     */
    @Test
    void testGetString_InvalidKey() {
        String key = "invalidKey";
        String expectedValue = "!" + key + "!";
        assertEquals(expectedValue, I18n.getString(key));
    }

    /**
     * Test method for {@link I18n#getString(String, Object...)}.
     * Validates the formatted value returned for a valid key with parameters.
     */
    @Test
    void testGetStringWithParams_ValidKey() {
        String key = "action.archiving";
        String param = "test_url.ch";
        String expectedValue = "Archiving test_url.ch ...";
        assertEquals(expectedValue, I18n.getString(key, param));
    }

    /**
     * Test method for {@link I18n#getString(String, Object...)}.
     * Validates the value returned when an invalid key with parameters is provided.
     */
    @Test
    void testGetStringWithParams_InvalidKey() {
        String key = "invalidGreeting";
        String param = "Alice";
        String expectedValue = "!" + key + "!";
        assertEquals(expectedValue, I18n.getString(key, param));
    }
}
