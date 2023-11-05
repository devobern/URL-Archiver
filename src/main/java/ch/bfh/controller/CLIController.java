package ch.bfh.controller;

import ch.bfh.exceptions.FileModelException;
import ch.bfh.exceptions.PathValidationException;
import ch.bfh.helper.FileValidator;
import ch.bfh.helper.PathValidator;
import ch.bfh.model.URLArchiverModel;
import ch.bfh.model.URLPair;
import ch.bfh.model.UserChoice;
import ch.bfh.model.FolderModel;
import ch.bfh.model.FileModel;
import ch.bfh.view.ConsoleView;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;



/**
 * Command-Line Interface (CLI) controller for managing URLs.
 * This class interacts with users through the console to perform operations
 * like extracting and archiving URLs.
 */
public class CLIController {

    private final FileModel fileModel;
    private final ConsoleView view;
    private final URLExtractor extractor;
    private final URLArchiver archiver;
    private int currentURLPairIndex;
    private final Scanner scanner;

    /**
     * Constructs a new CLIController with the specified model, view, extractor, and archiver.
     *
     * @param locale     the current locale
     */
    public CLIController(Locale locale) {
        this.fileModel = null;
        this.view = new ConsoleView(locale);
        this.extractor = new URLExtractor();
        this.archiver = new URLArchiver();
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
        handlePath(path);
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

            // Todo: Use new Switch style
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
        fileModel.setArchivedURL(url, archivedURL);
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

    public void handlePath(String inputPath) {
        try {
            if (PathValidator.isFolder(inputPath)) {

                FolderModel folder = new FolderModel(inputPath);

                try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(folder.getBasePath()))) {
                    for (Path path : stream) {
                        if (!Files.isDirectory(path)) {
                            try {
                                String mimeType = FileValidator.validate(folder.getBasePath() + path.getFileName().toString());
                                view.printFormattedMessage("file.validated.info", path.getFileName().toString());
                                folder.addFile(new FileModel(folder.getBasePath() + path.getFileName().toString(), mimeType));
                            } catch (FileModelException e) {
                                view.printFormattedMessage("folder.skipFile.info", path.getFileName().toString());
                            }

                        }
                    }
                }

                fileModel.setUrlPairs(extractor.extractFromFolder(folder));


            } else {
                String mimeType = FileValidator.validate(inputPath);
                FileModel file = new FileModel(inputPath, mimeType);
                view.printFormattedMessage("file.validated.info", file.getFileName());

                fileModel.setUrlPairs(extractor.extractFromFile(file));


            }

        } catch (IOException e) {
            view.printMessage("file.read.error");
        } catch (PathValidationException e) {
            view.printMessage("path.invalid.error");
        } catch (FileModelException e) {
        view.printMessage("file.notSupported.error");
    }

    }

    /**
     * Retrieves the current URLPair based on the current index.
     *
     * @return the current URLPair
     */
    private URLPair getCurrentURLPair() {
        List<URLPair> pairs = fileModel.getUrlPairs();
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
        if (currentURLPairIndex < fileModel.getUrlPairs().size() - 1) {
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
