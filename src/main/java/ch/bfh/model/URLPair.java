package ch.bfh.model;

public class URLPair {
    private String extractedURL;
    private String archivedURL;
    private int lineNumber;

    public URLPair(String extractedURL, int lineNumber) {
        this.extractedURL = extractedURL;
        this.archivedURL = null;
        this.lineNumber = lineNumber;
    }

    public String getExtractedURL() {
        return extractedURL;
    }

    public String getArchivedURL() {
        return archivedURL;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setArchivedURL(String archivedURL) {
        this.archivedURL = archivedURL;
    }

    @Override
    public String toString() {
        return "URLPair { Extracted URL: " + extractedURL + ", Archived URL: " + archivedURL + ", Line: " + lineNumber + " }";
    }
}
