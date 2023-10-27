package ch.bfh.controller;

import ch.bfh.model.URLPair;

public interface URLArchiverController {
    void start(String[] args);
    void processUserInput();
    void handleOpen(URLPair currentURLPair);

    void handleArchive(String url);

    void handleNext();

    void handleQuit();

    void handleFilePath(String filePath);

    URLPair getCurrentURLPair();

    void moveToNextURLPair();

    void lastURLPair();

    // Other methods...
}
