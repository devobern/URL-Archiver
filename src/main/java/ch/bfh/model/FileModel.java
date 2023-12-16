package ch.bfh.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represents a file for URL processing, holding metadata and content-related URLs.
 */
public class FileModel {

    private final String mimeType;
    private final String fileName;
    private final Path filePath;
    private List<URLPair> urlPairs;


    /**
     * Constructs a FileModel with a path and MIME type.
     *
     * @param filePath the file's path
     * @param mimeType the file's MIME type
     */
    public FileModel(Path filePath, String mimeType) {
        this.mimeType = mimeType;
        this.filePath = filePath;
        this.fileName = filePath.getFileName().toString();
        this.urlPairs = new ArrayList<>();
    }

    /**
     * Returns the file name.
     *
     * @return the file name as a string
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Returns the MIME type of the file.
     *
     * @return the MIME type as a string
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Returns the file path.
     *
     * @return the file path as a Path object
     */
    public Path getFilePath() {
        return filePath;
    }


    /**
     * Returns the list of URL pairs.
     *
     * @return an unmodifiable list of URLPair objects
     */
    public List<URLPair> getUrlPairs() {
        if(this.urlPairs == null){
            return Collections.emptyList();
        }
        return this.urlPairs;
    }

    /**
     * Adds a list of extracted URLs to the URL pairs.
     *
     * @param extractedURLs a list of URLs as strings
     */
    public void addExtractedURLs(Set<String> extractedURLs) {
        for (String url:extractedURLs) {
            urlPairs.add(new URLPair(url));
        }
    }

    /**
     * Associates an archived URL with its corresponding extracted URL in the list of URL pairs.
     *
     * @param extractedURL the URL that was extracted from the file content
     * @param archivedURL  the URL that was archived
     */
    public void setArchivedURL(String extractedURL, List<String> archivedURLs) {
        for (URLPair pair : urlPairs) {
            if (pair.getExtractedURL().equals(extractedURL)) {
                pair.setArchivedURLs(archivedURLs);
                break;
            }
        }
    }

    /**
     * Associates an archived URL with its corresponding extracted URL in the list of URL pairs.
     *
     * @param extractedURL the URL that was extracted from the file content
     * @param archivedURL  the URL that was archived
     */
    public void addArchivedURL(String extractedURL, String archivedURL) {
        for (URLPair pair : urlPairs) {
            if (pair.getExtractedURL().equals(extractedURL)) {
                pair.addArchivedURL(archivedURL);
                break;
            }
        }
    }

}
