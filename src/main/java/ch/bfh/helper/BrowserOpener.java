package ch.bfh.helper;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BrowserOpener {

    /**
     * Opens the specified URL in the user's default browser.
     *
     * @param urlString The URL to open, correctly formatted with a scheme.
     * @throws IOException if there is an issue launching the browser.
     * @throws URISyntaxException if the URL string is not properly formatted.
     */
    public static void openURL(String urlString) throws IOException, URISyntaxException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                // Add "http://" to the URL if it's missing
                if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
                    urlString = "http://" + urlString;
                }
                URI uri = new URI(urlString);
                desktop.browse(uri);
            } else {
                throw new UnsupportedOperationException("BROWSE action not supported on current platform");
            }
        } else {
            throw new UnsupportedOperationException("Desktop is not supported on current platform");
        }
    }
}

