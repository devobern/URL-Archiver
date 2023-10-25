package ch.bfh.helper;

import ch.bfh.model.URLArchiverModel;
import ch.bfh.model.URLPair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class URLExtractor {

    /**
     * Extracts URLs from a file at the given path.
     *
     * @param filePath the path to the file from which URLs should be extracted.
     * @return a list of extracted URLs.
     * @throws IOException if there's an issue reading the file.
     */
    public List<URLPair> extractFromFile(String filePath) throws IOException {
        List<URLPair> extractedUrls = new ArrayList<>();

        // todo: Actual logic here
        URLPair urlPairExample = new URLPair("example.com", 1);
        extractedUrls.add(urlPairExample);

        return extractedUrls;
    }
}
