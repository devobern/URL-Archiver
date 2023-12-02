package ch.bfh.helper;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The {@code BrowserOpener} class provides functionality to open URLs in the
 * user's default web browser. It ensures the URL is opened in a supported
 * environment and handles basic URL validation.
 */
public class BrowserOpener {

    /**
     * Opens the specified URL in the user's default browser.
     *
     * @param urlString The URL to open, correctly formatted with a scheme.
     * @throws IOException if there is an issue launching the browser.
     * @throws URISyntaxException if the URL string is not properly formatted.
     */
    public static void openURL(String urlString) throws IOException, URISyntaxException {
        if (!Desktop.isDesktopSupported()) {
            throw new UnsupportedOperationException("Desktop is not supported on current platform");
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            throw new UnsupportedOperationException("BROWSE action not supported on current platform");
        }

        urlString = validateAndFormatURL(urlString);
        URI uri = new URI(urlString);
        desktop.browse(uri);
    }

    /**
     * Validates and formats the URL. Adds "http://" if the scheme is missing.
     *
     * @param urlString The URL to validate and format.
     * @return The validated and formatted URL.
     */
    private static String validateAndFormatURL(String urlString) {
        try {
            new URI(urlString); // Validate URL
        } catch (Exception e) {
            urlString = "http://" + urlString; // Add default scheme if invalid
        }
        return urlString;
    }
}