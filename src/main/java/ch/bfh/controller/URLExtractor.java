package ch.bfh.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class URLExtractor {
    /**
     * Extracts URLs from a file at the given path.
     *
     * @param fileContent the content of the file from which urls should be extracted.
     * @return a list of extracted URLs.
     * @throws IOException if there's an issue reading the file.
     */
    public Set<String> extractFromFile(String fileContent) throws IOException {
        Set<String> extractedUrls = new HashSet<>();



        // TODO: remove print
        //System.out.println(file.fileToString());

        // todo: Actual logic here
        String urlExample = "example.com";
        extractedUrls.add(urlExample);

        return extractedUrls;
    }

    // todo: We do not need this function anymore. I guess...
    /**
    public List<URLPair> extractFromFolder(FolderModel folder) throws IOException {
        List<URLPair> extractedUrls = new ArrayList<>();

        try {
            while(!folder.wasLastFile()) {
                extractedUrls.addAll(extractFromFile(folder.next()));
            }
        } catch (FolderModelException e) {
            // shouldn't be thrown if correctly iterated
        }


        return  extractedUrls;
    }
     **/
}
