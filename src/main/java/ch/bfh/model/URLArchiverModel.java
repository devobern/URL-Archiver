package ch.bfh.model;

import java.util.ArrayList;
import java.util.List;

public class URLArchiverModel {

    private List<URLPair> urlPairs;

    public URLArchiverModel() {
        this.urlPairs = new ArrayList<>();
    }

    public void setUrlPairs(List<URLPair> urlPairs) {
        this.urlPairs = urlPairs;
    }

    public List<URLPair> getUrlPairs() {
        return urlPairs;
    }


    public void addExtractedURL(String extractedURL, int lineNumber) {
        urlPairs.add(new URLPair(extractedURL, lineNumber));
    }

    public void setArchivedURL(String extractedURL, String archivedURL) {
        for (URLPair pair : urlPairs) {
            if (pair.getExtractedURL().equals(extractedURL)) {
                pair.setArchivedURL(archivedURL);
                break;
            }
        }
    }
}
