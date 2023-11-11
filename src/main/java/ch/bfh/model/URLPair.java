package ch.bfh.model;

// todo: We need something to store the context, where the url is in the file. Important for the .bib file processing!
/**
 * Represents a pair of URLs: one extracted from a source and its corresponding archived version.
 */
public class URLPair {
    private String extractedURL;
    private String archivedURL;
    private final int lineNumber;

    /**
     * Constructs a URLPair with the given extracted URL and its line number.
     *
     * @param extractedURL the URL extracted from a source
     */
    public URLPair(String extractedURL) {
        this.extractedURL = extractedURL;
        this.archivedURL = null;
        this.lineNumber = 0;
    }

    /**
     * Retrieves the extracted URL.
     *
     * @return the extracted URL
     */
    public String getExtractedURL() {
        return extractedURL;
    }

    /**
     * Retrieves the archived URL.
     *
     * @return the archived URL or null if not set
     */
    public String getArchivedURL() {
        return archivedURL;
    }

    /**
     * Retrieves the line number where the URL was extracted from.
     *
     * @return the line number
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Sets the archived URL.
     *
     * @param archivedURL the archived URL to be set
     */
    public void setArchivedURL(String archivedURL) {
        this.archivedURL = archivedURL;
    }


    /**
     * Returns a string representation of the URL pair.
     *
     * @return a formatted string representing the URL pair
     */
    @Override
    public String toString() {
        return "URLPair { Extracted URL: " + extractedURL + ", Archived URL: " + archivedURL + ", Line: " + lineNumber + " }";
    }
}
