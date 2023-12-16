package ch.bfh.controller;

import ch.bfh.archiver.*;
import ch.bfh.exceptions.*;
import ch.bfh.helper.*;
import ch.bfh.model.*;
import ch.bfh.model.filereader.FileReaderFactory;
import ch.bfh.model.filereader.FileReaderInterface;
import ch.bfh.view.ConsoleView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Command-Line Interface (CLI) controller for managing URLs.
 * This class interacts with users through the console to perform operations
 * like extracting and archiving URLs.
 */
public class CLIController {

    private FileModel fileModel;
    private int currentURLPairIndex;
    private FolderModel folderModel;
    private int currentFileIndex;
    private final ConsoleView view;
    private final Scanner scanner;
    private boolean running = true;
    private final ArchiverManager archiverManager;
    private ConfigModel config;

    /**
     * Initializes a new controller for the command-line interface. This controller manages the user interface for URL extraction and archiving operations.
     * It sets up the necessary view, extractor, archiver, and scanner components based on the provided locale.
     *
     * @param locale the locale to use for user interface messages
     */
    public CLIController(Locale locale) {
        // Initialization of components with the provided locale
        this.view = new ConsoleView(locale);
        this.scanner = new Scanner(System.in);

        // Default state initialization
        this.fileModel = null;
        this.currentURLPairIndex = 0;
        this.folderModel = null;
        this.currentFileIndex = 0;

        // read config file
        try {
            this.config = ConfigFileHelper.read();
        } catch (ConfigFileException e) {
            this.config = new ConfigModel();
            view.printFormattedMessage("config.read.error", e.getMessage());
        }

        // Create a Archiver Manager
        this.archiverManager = new ArchiverManager();
        // Initialize all possible archivers once

        archiverManager.addArchiver(new WaybackMachineArchiver(this.config));
        archiverManager.addArchiver(new ArchiveTodayArchiver());
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
                view.printMessage(e);
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
            String extractedURL = fileModel.getUrlPairs().get(currentURLPairIndex).getExtractedURL();
            List<String> archivedURL = fileModel.getUrlPairs().get(currentURLPairIndex).getArchivedURLs();

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
                    case OPEN -> handleOpen();
                    case ARCHIVE -> handleArchive(extractedURL);
                    case NEXT -> handleNext();
                    case HELP -> view.printOptions();
                    case CONFIG -> handleConfig();
                    case SHOW_ARCHIVED -> handleShowArchived();
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

    private void handleShowArchived() {
        view.printFormattedMessage("info.show_archived");

        // Create and populate a map of file names to lists of URLPairs with non-null archived URLs
        Map<String, List<URLPair>> fileUrlMap = (folderModel != null ? folderModel.getFiles() : Collections.singletonList(fileModel)).stream()
                .collect(Collectors.toMap(
                        FileModel::getFileName,
                        fm -> fm.getUrlPairs().stream()
                                .filter(urlPair -> urlPair.getArchivedURLs() != null && !urlPair.getArchivedURLs().isEmpty())
                                .collect(Collectors.toList())
                ));

        // Print map
        fileUrlMap.forEach((fileName, urlPairs) -> {
            view.printFormattedMessage("info.file_name", fileName);
            urlPairs.forEach(urlPair -> {
                String extractedUrl = urlPair.getExtractedURL();
                urlPair.getArchivedURLs().forEach(archivedUrl ->
                        view.printFormattedMessage("info.extracted_archived_url", extractedUrl, archivedUrl)
                );
            });
        });

        // Ask user if they want to open the archived URLs
        view.printFormattedMessage("info.open_archived");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            fileUrlMap.values().stream()
                    .flatMap(List::stream)
                    .flatMap(urlPair -> urlPair.getArchivedURLs().stream())
                    .forEach(this::openURL);
        }
    }

    private void handleConfig() {
        view.printFormattedMessage("config.modify.print", this.config.getAccessKey(), this.config.getSecretKey(), this.config.getBrowser().name());
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("y")) {
            handleModifyConfig();
        }
    }

    private void handleModifyConfig() {
        view.printFormattedMessage("config.modify.accessKey", this.config.getAccessKey());
        String inputAccessKey = scanner.nextLine();
        if (!inputAccessKey.isEmpty() && !inputAccessKey.isBlank()) {
            this.config.setAccessKey(inputAccessKey);
        }

        view.printFormattedMessage("config.modify.secretKey", this.config.getSecretKey());
        String inputSecretKey = scanner.nextLine();
        if (!inputSecretKey.isEmpty() && !inputSecretKey.isBlank()) {
            this.config.setSecretKey(inputSecretKey);
        }

        boolean supportedBrowser = false;


        view.printFormattedMessage("config.modify.browser", this.config.getBrowser().name());
        String inputBrowser = scanner.nextLine();

        switch (inputBrowser.toUpperCase()) {
            case "FIREFOX":
                this.config.setBrowser(SupportedBrowsers.FIREFOX);
                supportedBrowser = true;
                break;
            case "EDGE":
                this.config.setBrowser(SupportedBrowsers.EDGE);
                supportedBrowser = true;
                break;
            case "CHROME":
                this.config.setBrowser(SupportedBrowsers.CHROME);
                supportedBrowser = true;
                break;
            case "DEFAULT":
                this.config.setBrowser(SupportedBrowsers.DEFAULT);
                supportedBrowser = true;
                break;
            case "":
                supportedBrowser = true;
                break;
            default:
        }
        while (!supportedBrowser) {
            view.printFormattedMessage("config.modify.invalidBrowser", inputBrowser);
            inputBrowser = scanner.nextLine();

            switch (inputBrowser.toUpperCase()) {
                case "FIREFOX":
                    this.config.setBrowser(SupportedBrowsers.FIREFOX);
                    supportedBrowser = true;
                    break;
                case "EDGE":
                    this.config.setBrowser(SupportedBrowsers.EDGE);
                    supportedBrowser = true;
                    break;
                case "CHROME":
                    this.config.setBrowser(SupportedBrowsers.CHROME);
                    supportedBrowser = true;
                    break;
                case "DEFAULT":
                    this.config.setBrowser(SupportedBrowsers.DEFAULT);
                    supportedBrowser = true;
                    break;
                case "":
                    supportedBrowser = true;
                    break;
                default:
            }
        }

        try {
            ConfigFileHelper.save(this.config);
            view.printMessage("config.save.success");
            view.printSeparator();
        } catch (ConfigFileException e) {
            view.printMessage(e);
        }


    }

    /**
     * Opens the currently selected URL for viewing. If an archived version exists,
     * additional options are provided for opening either the original or the archived URL.
     */
    private void handleOpen() {
        if (fileModel.getUrlPairs().get(currentURLPairIndex).getArchivedURLs() != null) {
            handleOpenArchived();
        } else {
            String extractedURL = fileModel.getUrlPairs().get(currentURLPairIndex).getExtractedURL();
            view.printFormattedMessage("action.opening", extractedURL);

            // Open the URL in the users default browser
            openURL(extractedURL);
        }
    }

    /**
     * Attempts to open a given URL in the user's default web browser.
     * If an exception occurs, it displays an error message to the console.
     *
     * @param url The URL to open.
     */
    private void openURL(String url) {
        try {
            BrowserOpener.openURL(url);
        } catch (IOException e) {
            view.printFormattedMessage("error.ioexception", e.getMessage());
        } catch (URISyntaxException e) {
            view.printFormattedMessage("error.uriexception", e.getMessage());
        } catch (UnsupportedOperationException e) {
            view.printFormattedMessage(e.getMessage().contains("BROWSE action not supported") ? "error.browsenotsupported" : "error.desktopnotsupported");
        } catch (Exception e) {
            view.printFormattedMessage("error.genericexception", e.getMessage());
        }
    }


    /**
     * Prompts for and processes the user's choice to open either the original or archived URL
     * from the current URLPair.
     */
    private void handleOpenArchived() {
        view.printMessage("action.open_archived");
        String choice = scanner.nextLine();
        List<String> targetUrl = new ArrayList<>();
        switch (choice) {
            case "1" -> targetUrl.add(fileModel.getUrlPairs().get(currentURLPairIndex).getExtractedURL());
            case "2" -> targetUrl = fileModel.getUrlPairs().get(currentURLPairIndex).getArchivedURLs();
            default -> {
                view.printFormattedMessage("action.invalid");
                return;
            }
        }
        view.printFormattedMessage("action.opening", targetUrl);

        // Open the URL in the users default browser
        for(String url : targetUrl){
            openURL(url);
        }
    }

    /**
     * Handles the archiving of a specified URL. This method prompts the user to select
     * an archiving service, performs the archiving using the selected service(s), and
     * updates the model with the archived URL. It also informs the user about the
     * availability of the selected archiving services and displays the archived URL.
     *
     * @param url the URL to be archived
     */
    private void handleArchive(String url) {
        view.printFormattedMessage("action.archiving", url);

        // Prompt user to choose the archiving service
        view.printMessage("action.archiving.prompt");
        String serviceChoice = scanner.nextLine();

        List<URLArchiver> selectedArchivers = new ArrayList<>();

        switch (serviceChoice) {
            case "1" -> {
                if (this.config.getAccessKey().isEmpty() || this.config.getSecretKey().isEmpty()) {
                    view.printMessage("action.archiving.warning.noCredentials");
                    String choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("y")) {
                        handleConfig();
                        if (!this.config.getAccessKey().isEmpty() && !this.config.getSecretKey().isEmpty()) {
                            selectedArchivers.add(archiverManager.getArchiver("WaybackMachine"));
                        }
                    }
                } else {
                    selectedArchivers.add(archiverManager.getArchiver("WaybackMachine"));
                }

            }
            case "2" -> {
                view.printFormattedMessage("action.archiving.solve_captchas");
                selectedArchivers.add(archiverManager.getArchiver("ArchiveToday"));
            }
            case "3" -> selectedArchivers.addAll(archiverManager.getAllArchivers());
            default -> {
                view.printFormattedMessage("action.invalid");
                return;
            }
        }

        // Perform archiving with the selected archivers
        try {
            startArchiving();
            ArchiverResult result = archiverManager.archive(url, selectedArchivers);
            finishArchiving();

            if (result.archivedUrls().isEmpty()) {
                view.printFormattedMessage("action.archiving.error.no_archivers_available");
            } else {
                // You may want to handle multiple URLs here if "Both" was selected
                List<String> archivedUrls = result.archivedUrls();
                fileModel.setArchivedURL(url, archivedUrls);
                view.printFormattedMessage("info.archived_url", archivedUrls);
            }

            // Inform the user about each unavailable archiver
            for (String archiverName : result.unavailableArchivers()) {
                view.printFormattedMessage("action.archiving.error.archiver_unavailable", archiverName);
            }
        } catch (ArchiverException e) {
            view.printMessage(e);
            finishArchiving();
        }



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
        view.printMessage("action.export");
        String userInput = scanner.nextLine();
        if(userInput.equalsIgnoreCase("y")) {
            handleExport();
        }

        view.printMessage("action.quit");
        this.running = false;
        shutdown();
    }

    private void handleExport() {
        String path = "";
        // Check if the path is valid
        boolean isValid = false;
        while (!isValid) {
            try {
                view.printMessage("action.export.path");
                path = scanner.nextLine();
                PathValidator.validate(path);
                isValid = true;
            } catch (PathValidationException e) {
                if(!e.getMessage().equals(I18n.getString("path.notExist.error"))) {
                    view.printFormattedMessage("error.retry");
                    view.printMessage(e);
                } else {
                    isValid = true;
                }
            }
        }

        if (PathValidator.isFolder(path)) {
            if (!path.endsWith(File.separator)) {
                path = path + File.separator;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            path = path + "archiveURLs_" + simpleDateFormat.format(new Date(System.currentTimeMillis())) + ".csv";
        }

        // "overwrite" all file extensions with .csv
        if (!path.endsWith(".csv")) {
            path = path + ".csv";
        }

        if (this.folderModel == null) {
            try {
                URLExporter.exportUrlsToCSV(this.fileModel, path);
            } catch (FileNotFoundException | URLExporterException e) {
                view.printMessage(e);
            }
        } else {
            try {
                URLExporter.exportUrlsToCSV(this.folderModel, path);
            } catch (FileNotFoundException | URLExporterException e) {
                view.printMessage(e);
            }
        }
    }

    /**
     * Iterates through a directory to extract all files and adds them to the folder model.
     * It skips directories and only processes files that are valid according to the FileValidator.
     * Valid files are added to the folder model with their MIME type. Any errors encountered
     * during file validation or adding are logged for information purposes. IOExceptions
     * thrown during directory iteration are converted to RuntimeExceptions to simplify error handling.
     */
    private void extractFilesFromFolder() {
        // Iterate over the directory stream to process files
        Path directoryPath = Paths.get(folderModel.getBasePath());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {
            for (Path entry : stream) {
                Path fullPath = directoryPath.resolve(entry.getFileName());
                processFile(fullPath);
            }
        } catch (IOException e) {
            // Convert to unchecked exception to simplify error handling
            throw new RuntimeException("Error iterating through the directory", e);
        }
    }

    /**
     * Processes a single file filePath to validate and add to the folder model if it is not a directory.
     *
     * @param filePath The file filePath to process.
     */
    private void processFile(Path filePath) throws IOException {
        if (!Files.isDirectory(filePath)) {
            try {
                String mimeType = FileValidator.validate(filePath.toString());
                view.printFormattedMessage("file.validated.info", filePath.getFileName().toString());
                folderModel.addFile(new FileModel(filePath, mimeType));
            } catch (FileModelException e) {
                view.printMessage(e);
            }
        }
    }


    /**
     * Processes the given path by determining if it's a file or a folder and
     * populating the appropriate model with URL data extracted from the contents.
     *
     * @param inputPath the path to be processed
     */
    public void handlePath(String inputPath) {
        try {
            if (PathValidator.isFolder(inputPath)) {
                handleFolder(inputPath);
            } else {
                handleSingleFile(inputPath);
            }
        } catch (IOException e) {
            view.printMessage("file.read.error");
        } catch (FileModelException e) {
            view.printMessage(e);
        }
    }

    /**
     * Processes the contents of a folder path and initializes the folder model with the contents.
     *
     * @param folderPath the directory path to process
     * @throws IOException if an I/O error occurs when opening the directory
     */
    private void handleFolder(String folderPath) throws IOException {
        folderModel = new FolderModel(folderPath);
        extractFilesFromFolder();
        for(int i = 0; i < folderModel.getFiles().size(); i++){
            FileModel tempFileModel = folderModel.getFiles().get(i);
            // Delete file if it has no urls
            if(!processFileModel(tempFileModel)){
                folderModel.removeFile(i);
                i--;
            }
        }
        // Assume that folderModel.getFiles() is never null and has at least one file
        this.fileModel = folderModel.getFiles().get(0);
    }

    /**
     * Processes a single file path, validates it, and initializes the file model.
     *
     * @param filePath the path of the file to process
     * @throws IOException        if an I/O error occurs when reading the file
     * @throws FileModelException if the file model cannot be initialized
     */
    private void handleSingleFile(String filePath) throws IOException, FileModelException {
        Path validatedPath = Paths.get(filePath);
        String mimeType = FileValidator.validate(filePath);
        fileModel = new FileModel(validatedPath, mimeType);
        view.printFormattedMessage("file.validated.info", fileModel.getFileName());
        if(!processFileModel(fileModel)){
            handleQuit();
        }
    }

    /**
     * Processes the specified file model by reading its content, extracting URLs, and updating the model with these URLs.
     * @param fileModel The file model representing the file to be processed. It contains the file's path and MIME type.
     * @return true if URLs are successfully extracted and added to the file model, false if no URLs are found in the file.
     * @throws IOException If an error occurs during reading of the file's content, indicating an I/O problem.
     */
    private boolean processFileModel(FileModel fileModel) throws IOException {
        FileReaderInterface fileReader = FileReaderFactory.getFileReader(fileModel.getMimeType());
        String fileContent = fileReader.readFile(fileModel.getFilePath());
        Set<String> extractedURLs = URLExtractor.extractURLs(fileContent);
        if(extractedURLs.isEmpty()){
            return false;
        }
        fileModel.addExtractedURLs(extractedURLs);
        return true;
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
     * Initiates the start of the archiving process.
     * This private method is used internally to signal the start of
     * archiving, triggering the view to display the archiving indicator.
     */
    private void startArchiving() {
        view.startArchivingIndicator();
        // Start the archiving process
    }

    /**
     * Concludes the archiving process.
     * This private method is used internally to signal the end of
     * archiving, instructing the view to stop displaying the archiving indicator.
     */
    private void finishArchiving() {
        // End the archiving process
        view.stopArchivingIndicator();
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
