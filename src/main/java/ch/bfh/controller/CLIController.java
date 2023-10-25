package ch.bfh.controller;

import ch.bfh.exceptions.PathValidationException;
import ch.bfh.helper.PathValidator;
import ch.bfh.helper.URLArchiver;
import ch.bfh.helper.URLExtractor;
import ch.bfh.model.URLArchiverModel;
import ch.bfh.model.URLPair;
import ch.bfh.view.ConsoleView;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CLIController implements URLArchiverController {

    private URLArchiverModel model;
    private ConsoleView view;

    private URLExtractor extractor;
    private URLArchiver archiver;
    private int currentURLPairIndex;


    public CLIController(URLArchiverModel model, ConsoleView view, URLExtractor extractor, URLArchiver archiver) {
        this.model = model;
        this.view = view;
        this.extractor = extractor;
        this.archiver = archiver;
        this.currentURLPairIndex = 0;
    }

    @Override
    public void start(String[] args) {
        view.printWelcomeMessage();
        String path = null;

        // Check if the path is valid
        boolean isValid = false;
        while (!isValid) {
            path = (args.length > 0) ? args[0] : view.promptUserForPath();
            try {
                PathValidator.validate(path);
                // todo: remove
                System.out.println("Path ok: " + path);
                isValid = true;
            } catch (PathValidationException e) {
                System.out.println(e.getMessage());
                view.printFormattedMessage("error.retry");
            }
        }
        // Calls url extractor and saves url's to model
        handleFilePath(path);
        processUserInput();
    }

    @Override
    public void processUserInput() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            URLPair currentURLPair = getCurrentURLPair();
            String extractedURL = currentURLPair.getExtractedURL();
            String archivedURL = currentURLPair.getArchivedURL();

            view.printSeparator();
            view.printFormattedMessage("info.extracted_url", extractedURL);
            if (archivedURL != null) {
                view.printFormattedMessage("info.archived_url", archivedURL);
            }
            view.printMessage("instructions.options.prompt");
            String choice = scanner.nextLine();

            // todo: ENUM verwenden damit auch die Optionen internationalisierbar sind
            switch (choice.toLowerCase()) {
                case "o":
                    handleOpen(extractedURL, archivedURL);
                    break;
                case "a":
                    handleArchive(extractedURL);
                    break;
                case "n":
                    handleNext();
                    break;
                case "h":
                    view.printOptions();
                    break;
                case "q":
                    handleQuit();
                    running = false;
                    break;
                default:
                    view.printFormattedMessage("action.invalid");
            }
        }
    }

    /**
     * Handles the user's choice to open a URL.
     *
     * @param extractedURL The URL to be opened.
     */
    private void handleOpen(String extractedURL, String archivedURL) {
        // Logic to open URL
        String targetUrl = (archivedURL != null) ? archivedURL : extractedURL;
        view.printFormattedMessage("action.opening", targetUrl);
    }

    /**
     * Handles the user's choice to archive a URL.
     *
     * @param url The URL to be archived.
     */
    private void handleArchive(String url) {
        view.printFormattedMessage("action.archiving", url);
        String archivedURL = archiver.archiveURL(url);
        model.setArchivedURL(url, archivedURL);
        view.printFormattedMessage("info.archived_url", archivedURL);
    }

    /**
     * Handles the user's choice to move to the next URL.
     */
    private void handleNext() {
        moveToNextURLPair();
        view.printFormattedMessage("action.next");
    }

    /**
     * Handles the user's choice to quit the application.
     */
    private void handleQuit() {
        view.printMessage("action.quit");
    }

    public void handleFilePath(String filePath) {
        try {
            model.setUrlPairs(extractor.extractFromFile(filePath));
        } catch (IOException e) {
            view.printMessage("file.read.error");
        }

    }

    public URLPair getCurrentURLPair() {
        List<URLPair> pairs = model.getUrlPairs();
        if (currentURLPairIndex >= 0 && currentURLPairIndex < pairs.size()) {
            return pairs.get(currentURLPairIndex);
        }
        return null;
    }

    public void moveToNextURLPair() {
        if (currentURLPairIndex < model.getUrlPairs().size() - 1) {
            currentURLPairIndex++;
        }
    }

    public void moveToPreviousURLPair() {
        if (currentURLPairIndex > 0) {
            currentURLPairIndex--;
        }
    }

    public void resetURLPairCounter() {
        currentURLPairIndex = 0;
    }
}
