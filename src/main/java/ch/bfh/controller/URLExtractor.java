package ch.bfh.controller;

import ch.bfh.exceptions.FileModelException;
import ch.bfh.exceptions.FolderModelException;
import ch.bfh.exceptions.PathValidationException;
import ch.bfh.model.FileModel;
import ch.bfh.model.FolderModel;
import ch.bfh.model.URLArchiverModel;
import ch.bfh.model.URLPair;
import ch.bfh.view.ConsoleView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class URLExtractor {
    /**
     * Extracts URLs from a file at the given path.
     *
     * @param file the file object from which URLs should be extracted.
     * @return a list of extracted URLs.
     * @throws IOException if there's an issue reading the file.
     */
    public List<URLPair> extractFromFile(FileModel file) throws IOException {
        List<URLPair> extractedUrls = new ArrayList<>();



        // TODO: remove print
        System.out.println(file.fileToString());

        // todo: Actual logic here
        URLPair urlPairExample = new URLPair("example.com", 1);
        extractedUrls.add(urlPairExample);

        return extractedUrls;
    }

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
}
