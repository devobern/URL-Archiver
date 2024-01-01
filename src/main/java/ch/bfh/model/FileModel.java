package ch.bfh.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represents a file for URL processing, containing metadata and URLs related to its content.
 */
public class FileModel {

    private final String mimeType;
    private final String fileName;
    private final Path filePath;
    private final List<URLPair> urlPairs;

    /**
     * Constructs a FileModel with specified file path and MIME type.
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

    public String getFileName() {
        return this.fileName;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Path getFilePath() {
        return this.filePath;
    }

    /**
     * Retrieves an unmodifiable view of the URL pairs.
     *
     * @return an unmodifiable list of URLPair objects
     */
    public List<URLPair> getUrlPairs() {
        return Collections.unmodifiableList(this.urlPairs);
    }

    /**
     * Adds a set of extracted URLs to the URL pairs.
     *
     * @param extractedURLs a set of URLs
     */
    public void addExtractedURLs(Set<String> extractedURLs) {
        extractedURLs.forEach(url -> urlPairs.add(new URLPair(url)));
    }

    /**
     * Sets archived URLs for a corresponding extracted URL.
     *
     * @param extractedURL the URL extracted from the file
     * @param archivedURLs list of archived URLs
     */
    public void setArchivedURL(String extractedURL, List<String> archivedURLs) {
        urlPairs.stream()
                .filter(pair -> pair.getExtractedURL().equals(extractedURL))
                .findFirst()
                .ifPresent(pair -> pair.setArchivedURLs(archivedURLs));
    }

    /**
     * Adds an archived URL for a corresponding extracted URL.
     *
     * @param extractedURL the URL extracted from the file
     * @param archivedURL  the archived URL
     */
    public void addArchivedURL(String extractedURL, String archivedURL) {
        urlPairs.stream()
                .filter(pair -> pair.getExtractedURL().equals(extractedURL))
                .findFirst()
                .ifPresent(pair -> pair.addArchivedURL(archivedURL));
    }

    /**
     * Returns whether the file has any archived URLs.
     *
     * @return true if the file has archived URLs, false otherwise
     */
    public boolean hasArchivedURLs() {
        return urlPairs.stream().anyMatch(URLPair::hasArchivedURLs);
    }
}
