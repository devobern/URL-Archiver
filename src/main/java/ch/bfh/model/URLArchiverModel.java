package ch.bfh.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a collection of URL pairs (extracted and archived).
 */
public class URLArchiverModel {

    private List<URLPair> urlPairs;

    /**
     * Constructs a new URLArchiverModel with an empty list of URL pairs.
     */
    public URLArchiverModel() {
        this.urlPairs = new ArrayList<>();
    }

    /**
     * Sets the list of URL pairs.
     *
     * @param urlPairs the list of URL pairs to set
     */
    public void setUrlPairs(List<URLPair> urlPairs) {
        this.urlPairs = urlPairs;
    }

    /**
     * Retrieves the list of URL pairs.
     *
     * @return the list of URL pairs
     */
    public List<URLPair> getUrlPairs() {
        return urlPairs;
    }


    /**
     * Adds an extracted URL with its line number to the list.
     *
     * @param extractedURL the extracted URL to add
     * @param lineNumber   the line number where the URL was extracted from
     */
    public void addExtractedURL(String extractedURL, int lineNumber) {
        urlPairs.add(new URLPair(extractedURL, lineNumber));
    }

    /**
     * Sets the archived URL corresponding to the provided extracted URL.
     *
     * @param extractedURL the extracted URL for which the archived URL is to be set
     * @param archivedURL  the archived URL to be associated with the extracted URL
     */
    public void setArchivedURL(String extractedURL, String archivedURL) {
        for (URLPair pair : urlPairs) {
            if (pair.getExtractedURL().equals(extractedURL)) {
                pair.setArchivedURL(archivedURL);
                break;
            }
        }
    }
}
