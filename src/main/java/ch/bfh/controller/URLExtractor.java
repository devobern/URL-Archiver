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

    private ConsoleView view;

    public URLExtractor(ConsoleView view) {
        this.view = view;
    }

    /**
     * Extracts URLs from a file at the given path.
     *
     * @param filePath the path to the file from which URLs should be extracted.
     * @return a list of extracted URLs.
     * @throws IOException if there's an issue reading the file.
     */
    public List<URLPair> extractFromFile(String filePath) throws IOException, FileModelException {
        List<URLPair> extractedUrls = new ArrayList<>();

        FileModel file = new FileModel(filePath, this.view);

        // TODO: remove print
        System.out.println(file.fileToString());

        // todo: Actual logic here
        URLPair urlPairExample = new URLPair("example.com", 1);
        extractedUrls.add(urlPairExample);

        return extractedUrls;
    }

    public List<URLPair> extractFromFolder(String folderPath) throws IOException, PathValidationException, FolderModelException {
        List<URLPair> extractedUrls = new ArrayList<>();

        FolderModel folder = new FolderModel(folderPath, this.view);

        // TODO: remove print
        while(!folder.wasLastFile()) {
            System.out.println(folder.next());
        }

        // todo: Actual logic here
        URLPair urlPairExample = new URLPair("example.com", 1);
        extractedUrls.add(urlPairExample);

        return  extractedUrls;
    }
}
