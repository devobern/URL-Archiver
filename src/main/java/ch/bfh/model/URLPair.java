package ch.bfh.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a pair of URLs: one extracted from a source and its corresponding archived versions.
 */
public class URLPair {
    private String extractedURL;
    private List<String> archivedURLs;

    /**
     * Constructs a URLPair with the specified extracted URL.
     *
     * @param extractedURL the URL extracted from a source
     */
    public URLPair(String extractedURL) {
        this.extractedURL = extractedURL;
        this.archivedURLs = new ArrayList<>();
    }

    public String getExtractedURL() {
        return extractedURL;
    }

    public List<String> getArchivedURLs() {
        return archivedURLs;
    }

    /**
     * Sets the list of archived URLs.
     *
     * @param archivedURLs the list of archived URLs
     */
    public void setArchivedURLs(List<String> archivedURLs) {
        this.archivedURLs = new ArrayList<>(archivedURLs);
    }

    /**
     * Adds an archived URL to the list. Replaces "pending" URLs if the new URL is from the web archive.
     *
     * @param archivedURL the archived URL to add
     */
    public void addArchivedURL(String archivedURL) {
        if (archivedURL.startsWith("https://web.archive.org")) {
            replacePendingURLs(archivedURL);
        } else {
            this.archivedURLs.add(archivedURL);
        }
    }

    /**
     * Replaces any "pending" URLs in the archived URLs list with the specified URL.
     *
     * @param url the URL to replace "pending" entries
     */
    private void replacePendingURLs(String url) {
        for (int i = 0; i < this.archivedURLs.size(); i++) {
            if (this.archivedURLs.get(i).equalsIgnoreCase("pending")) {
                this.archivedURLs.set(i, url);
                return;
            }
        }
        this.archivedURLs.add(url);
    }

    @Override
    public String toString() {
        return "URLPair { Extracted URL: " + extractedURL + ", Archived URLs: " + archivedURLs + " }";
    }
}
