package ch.bfh.controller;

import ch.bfh.exceptions.PathValidationException;
import ch.bfh.helper.PathValidator;
import ch.bfh.helper.URLArchiver;
import ch.bfh.helper.URLExtractor;
import ch.bfh.model.URLArchiverModel;
import ch.bfh.model.URLPair;
import ch.bfh.model.UserChoice;
import ch.bfh.view.ConsoleView;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;



/**
 * Command-Line Interface (CLI) controller for managing URLs.
 * This class interacts with users through the console to perform operations
 * like extracting and archiving URLs.
 */
public class CLIController {

    private final URLArchiverModel model;
    private final ConsoleView view;

    private final URLExtractor extractor;
    private final URLArchiver archiver;
    private int currentURLPairIndex;
    private final Scanner scanner;

    /**
     * Constructs a new CLIController with the specified model, view, extractor, and archiver.
     *
     * @param model     the URL archiver model
     * @param view      the console view
     * @param extractor the URL extractor
     * @param archiver  the URL archiver
     */
    public CLIController(URLArchiverModel model, ConsoleView view, URLExtractor extractor, URLArchiver archiver) {
        this.model = model;
        this.view = view;
        this.extractor = extractor;
        this.archiver = archiver;
        this.currentURLPairIndex = 0;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Initiates the URL management process by welcoming the user, validating the path,
     * handling the given file path, and processing the user's input.
     *
     * @param args the command-line arguments
     */
    public void start(String[] args) {
        view.printWelcomeMessage();
        String path = null;

        // Check if the path is valid
        boolean isValid = false;
        while (!isValid) {
            path = (args.length > 0) ? args[0] : view.promptUserForPath();
            try {
                PathValidator.validate(path);
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

    /**
     * Processes user input to execute various operations like opening,
     * archiving, moving to next URL, etc.
     */
    private void processUserInput() {

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
            view.promptUserForOption();
            String choice = scanner.nextLine();

            UserChoice userChoice = UserChoice.fromCommand(choice.toLowerCase());

            if (userChoice != null) {
                switch (userChoice) {
                    case OPEN:
                        handleOpen(currentURLPair);
                        break;
                    case ARCHIVE:
                        handleArchive(extractedURL);
                        break;
                    case NEXT:
                        handleNext();
                        break;
                    case HELP:
                        view.printOptions();
                        break;
                    case QUIT:
                        handleQuit();
                        running = false;
                        shutdown();
                        break;
                }
            } else {
                view.printFormattedMessage("action.invalid");
            }

        }
    }

    /**
     * Handles the action to open the specified URLPair. If the URLPair has an archived URL,
     * it prompts the user for further options.
     *
     * @param currentURLPair the URLPair to be opened
     */
    private void handleOpen(URLPair currentURLPair) {
        if (currentURLPair.getArchivedURL() != null) {
            handleOpenArchived(currentURLPair);
        } else {
            view.printFormattedMessage("action.opening", currentURLPair.getExtractedURL());
            // todo: Logic to open URL
        }
    }

    /**
     * Handles the action to open an archived URL from the specified URLPair.
     *
     * @param currentURLPair the URLPair containing the archived URL
     */
    private void handleOpenArchived(URLPair currentURLPair) {
        view.printMessage("action.open_archived");
        String choice = scanner.nextLine();
        String targetUrl = null;
        switch (choice) {
            case "1":
                targetUrl = currentURLPair.getExtractedURL();
                break;
            case "2":
                targetUrl = currentURLPair.getArchivedURL();
                break;
            default:
                view.printFormattedMessage("action.invalid");
                return;
        }
        view.printFormattedMessage("action.opening", targetUrl);
        // todo: Logic to open URL
    }

    /**
     * Archives the specified URL and updates the model.
     *
     * @param url the URL to be archived
     */
    private void handleArchive(String url) {
        view.printFormattedMessage("action.archiving", url);
        String archivedURL = archiver.archiveURL(url);
        model.setArchivedURL(url, archivedURL);
        view.printFormattedMessage("info.archived_url", archivedURL);
    }

    /**
     * Handles the action to move to the next URLPair.
     */
    private void handleNext() {
        moveToNextURLPair();
        view.printFormattedMessage("action.next");
    }

    /**
     * Notifies the user that the application is quitting.
     */
    private void handleQuit() {
        view.printMessage("action.quit");
    }

    /**
     * Processes the given file path to extract URLs and sets them to the model.
     *
     * @param filePath the path to the file containing URLs
     */
    private void handleFilePath(String filePath) {
        try {
            model.setUrlPairs(extractor.extractFromFile(filePath));
        } catch (IOException e) {
            view.printMessage("file.read.error");
        }

    }

    /**
     * Retrieves the current URLPair based on the current index.
     *
     * @return the current URLPair
     */
    private URLPair getCurrentURLPair() {
        List<URLPair> pairs = model.getUrlPairs();
        if (currentURLPairIndex >= 0 && currentURLPairIndex < pairs.size()) {
            return pairs.get(currentURLPairIndex);
        }
        return null;
    }

    /**
     * Moves the internal counter to the next URLPair. If it's the last URLPair,
     * the user is notified and the application quits.
     */
    private void moveToNextURLPair() {
        if (currentURLPairIndex < model.getUrlPairs().size() - 1) {
            currentURLPairIndex++;
            return;
        }
        lastURLPair();
    }

    /**
     * Notifies the user that they have reached the end of the URLPairs and prepares to quit the application.
     */
    private void lastURLPair(){
        view.printMessage("action.end");
        // todo: Handle write to .BIB
        handleQuit();
    }

    /**
     * Moves the internal counter to the previous URLPair.
     */
    private void moveToPreviousURLPair() {
        if (currentURLPairIndex > 0) {
            currentURLPairIndex--;
        }
    }

    /**
     * Resets the internal counter of the URLPair to the starting position.
     */
    private void resetURLPairCounter() {
        currentURLPairIndex = 0;
    }

    /**
     * Performs the necessary shutdown operations, such as closing resources.
     */
    private void shutdown() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
