package ch.bfh.controller;

import ch.bfh.exceptions.FileModelException;
import ch.bfh.exceptions.PathValidationException;
import ch.bfh.helper.FileValidator;
import ch.bfh.helper.PathValidator;
import ch.bfh.model.*;
import ch.bfh.view.ConsoleView;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private boolean running = true;

    /**
     * Initializes a new controller for the command-line interface. This controller manages the user interface for URL extraction and archiving operations.
     * It sets up the necessary view, extractor, archiver, and scanner components based on the provided locale.
     *
     * @param locale the locale to use for user interface messages
     */
    public CLIController(Locale locale) {
        // Initialization of components with the provided locale
        this.view = new ConsoleView(locale);
        this.extractor = new URLExtractor();
        this.archiver = new URLArchiver();
        this.scanner = new Scanner(System.in);

        // Default state initialization
        this.fileModel = null;
        this.currentURLPairIndex = 0;
        this.folderModel = null;
        this.currentFileIndex = 0;
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
                view.printFormattedMessage("error.retry");
            }
        }
        // Calls url extractor and saves hurl's to model
        handlePath(path);
        processUserInput();
    }

    /**
     * Manages the interactive command loop to process user commands for URL management.
     * This includes opening URLs, archiving them, navigating to the next or previous URL,
     * requesting help, or quitting the application. The method handles user input and
     * executes the corresponding actions until the user chooses to quit.
     */
    private void processUserInput() {


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
                    case OPEN -> handleOpen(currentURLPair);
                    case ARCHIVE -> handleArchive(extractedURL);
                    case NEXT -> handleNext();
                    case HELP -> view.printOptions();
                    case QUIT -> {
                        handleQuit();
                        running = false;
                    }
                }
            } else {
                view.printFormattedMessage("action.invalid");
            }
        }
    }

    /**
     * Opens the currently selected URL for viewing. If an archived version exists,
     * additional options are provided for opening either the original or the archived URL.
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
     * Prompts for and processes the user's choice to open either the original or archived URL
     * from the current URLPair.
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
     * Moves the internal counter to the next URLPair. If it's the last URLPair in the current file,
     * it checks for more files in the folder. If more files are present, it moves to the next file
     * and resets the URLPair index; otherwise, it notifies the user that the process is completed.
     */
    private void handleNext() {
        if (!moveToNextURLPair()) { // If it returns false, it means we are at the last URLPair.
            view.printFormattedMessage("action.end_of_urls_in_file");

            if (folderModel != null && currentFileIndex < folderModel.getFiles().size() - 1) {
                currentFileIndex++; // Move to the next file in the folder.
                currentURLPairIndex = 0; // Reset the URLPair index for the new file.
                fileModel = folderModel.getFiles().get(currentFileIndex); // Set the new fileModel.
                view.printFormattedMessage("action.next_file", fileModel.getFileName());
                // Process the new fileModel here or prompt user to continue with the new file.
            } else {
                // No more files left to process.
                view.printFormattedMessage("action.end_of_folder");
                handleQuit();
            }
        } else {
            // Moved to next URL pair within the same file.
            view.printFormattedMessage("action.next_url");
        }
    }


    /**
     * Notifies the user that the application is quitting.
     */
    private void handleQuit() {
        view.printMessage("action.quit");
        this.running = false;
        shutdown();
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
     * Processes a single file path, validates it, and initializes the file model.
     *
     * @param filePath the path of the file to process
     * @throws IOException if an I/O error occurs when reading the file
     * @throws FileModelException if the file model cannot be initialized
     */
    private URLPair getCurrentURLPair() {
        List<URLPair> pairs = fileModel.getUrlPairs();
        if (currentURLPairIndex >= 0 && currentURLPairIndex < pairs.size()) {
            return pairs.get(currentURLPairIndex);
        }
        return null;
    }

    /**
     * Attempts to move to the next URL pair and returns true if successful. If not,
     * it means it was the last URLPair, and further action may be needed.
     *
     * @return boolean - true if it moved to the next URLPair, false if it was the last one.
     */
    private boolean moveToNextURLPair() {
        if (currentURLPairIndex < fileModel.getUrlPairs().size() - 1) {
            currentURLPairIndex++;
            return true;
        }
        return false; // Return false to indicate it's the last URLPair.
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
     * Performs the necessary shutdown operations, such as closing resources.
     */
    private void shutdown() {
        scanner.close();
    }
}
