package ch.bfh.controller;

import ch.bfh.archiver.*;
import ch.bfh.exceptions.*;
import ch.bfh.helper.*;
import ch.bfh.model.*;
import ch.bfh.model.archiving.PendingWaybackMachineJob;
import ch.bfh.model.export.ExporterFactory;
import ch.bfh.model.filereader.FileReaderFactory;
import ch.bfh.model.filereader.FileReaderInterface;
import ch.bfh.view.ConsoleView;

import java.io.File;
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

    private final ConsoleView view;
    private final Scanner scanner;
    private final ArchiverManager archiverManager;
    private final ArrayList<PendingWaybackMachineJob> pendingJobs;
    private FileModel fileModel;
    private int currentURLPairIndex;
    private FolderModel folderModel;
    private int currentFileIndex;
    private boolean running = true;
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
        this.pendingJobs = new ArrayList<>();

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

        archiverManager.addArchiver(new WaybackMachineArchiver(this.config, this));
        archiverManager.addArchiver(new ArchiveTodayArchiver());
    }

    public FileModel getFileModel() {
        return fileModel;
    }

    public ArrayList<PendingWaybackMachineJob> getPendingJobs() {
        return pendingJobs;
    }

    public void addPendingJob(PendingWaybackMachineJob job) {
        this.pendingJobs.add(job);
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

            view.printSeparator();
            view.printFormattedMessage("info.current_file", fileModel.getFileName());
            view.printFormattedMessage("info.extracted_url", extractedURL);

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
                    case UPDATE_JOBS -> handleUpdateJobs();
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

    private void handleUpdateJobs() {
        statusUpdate();
        showPendingJobs();
    }

    private void handleShowArchived() {
        statusUpdate();

        // Create and populate a map of file names to lists of URLPairs with non-null archived URLs
        Map<String, List<URLPair>> fileUrlMap = (folderModel != null ? folderModel.getFiles() : Collections.singletonList(fileModel)).stream()
                .collect(Collectors.toMap(
                        FileModel::getFileName,
                        fm -> fm.getUrlPairs().stream()
                                .filter(urlPair -> urlPair.getArchivedURLs() != null && !urlPair.getArchivedURLs().isEmpty())
                                .collect(Collectors.toList())
                ));

        // Return if no archived URLs are found
        if(fileUrlMap.values().stream().allMatch(List::isEmpty)){
            view.printFormattedMessage("info.no_archived");
            return;
        }

        view.printFormattedMessage("info.show_archived");

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
                    .filter(url -> !url.equalsIgnoreCase("pending"))
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
        String extractedURL = fileModel.getUrlPairs().get(currentURLPairIndex).getExtractedURL();
        view.printFormattedMessage("action.opening", extractedURL);

        // Open the URL in the users default browser
        openURL(extractedURL);
        statusUpdate();
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
            case "3" -> selectedArchivers.addAll(archiverManager.getSortedArchivers());
            case "4" -> {
                return;
            }
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
                List<String> archivedUrls = result.archivedUrls();
                for (String archivedUrl : archivedUrls) {
                    fileModel.addArchivedURL(url, archivedUrl);
                }
            }

            // Inform the user about each unavailable archiver
            for (String archiverName : result.unavailableArchivers()) {
                view.printFormattedMessage("action.archiving.error.archiver_unavailable", archiverName);
            }
        } catch (ArchiverException e) {
            view.printMessage(e);
            System.out.println(e.getCause().getMessage());
            finishArchiving();
        }
        statusUpdate();

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
        statusUpdate();
    }


    /**
     * Notifies the user that the application is quitting.
     */
    private void handleQuit() {
        statusUpdate();

        // Check if there are any archived URLs using Stream API
        boolean archivedURLs = (folderModel != null) ?
                folderModel.getFiles().stream().anyMatch(FileModel::hasArchivedURLs) :
                fileModel.hasArchivedURLs();

        // Handle export
        if (fileModel != null && archivedURLs) {
            handleExport();
        }

        // Quit the application
        view.printMessage("action.quit");
        this.running = false;
        shutdown();
    }

    /**
     * Exports the URLs of the given file to a BIB file.
     *
     * @param fm the file to export the URLs from
     */
    private void exportBib(FileModel fm) {
        if (yesNoPromt("action.export.bib", fm.getFileName())) {
            try {
                ExporterFactory.getExporter("bib").exportURLs(fm, fm.getFilePath().toString());
            } catch (IOException e) {
                view.printMessage(e.getMessage());
            } catch (URLExporterException e) {
                view.printMessage(e);
            }
        }
    }

    /**
     * Checks if the given file is a BIB file.
     *
     * @param fileModel the file to check
     * @return true if the file is a BIB file, false otherwise
     */
    private boolean isBibFile(FileModel fileModel) {
        return Objects.equals(fileModel.getMimeType(), "text/bib") || Objects.equals(fileModel.getMimeType(), "text/x-bibtex");
    }

    /**
     * Prompts the user for a yes/no answer to the given message.
     *
     * @param message the message to display
     * @param args    the arguments to format the message with
     * @return true if the user answered yes, false otherwise
     */
    private boolean yesNoPromt(String message, Object... args) {
        view.printFormattedMessage(message, args);
        String userInput = scanner.nextLine();
        return userInput.equalsIgnoreCase("y");
    }

    private void handleExport() {
        // BIB Export
        if (folderModel != null) {
            for (FileModel fm : folderModel.getFiles()) {
                if (isBibFile(fm)) {
                    exportBib(fm);
                }
            }
        } else {
            if (isBibFile(fileModel)) {
                exportBib(fileModel);
            }
        }

        //CSV Export
        if (!yesNoPromt("action.export.csv")) {
            return;
        }

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
                if (!e.getMessage().equals(I18n.getString("path.notExist.error"))) {
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
                ExporterFactory.getExporter("csv").exportURLs(this.fileModel, path);
            } catch (IOException | URLExporterException e) {
                view.printMessage(e);
            }
        } else {
            try {
                ExporterFactory.getExporter("csv").exportURLs(this.folderModel, path);
            } catch (IOException | URLExporterException e) {
                view.printMessage(e);
            }
        }
    }

    /**
     * updates the status of the pending jobs
     */
    private void statusUpdate() {
        try {
            ((WaybackMachineArchiver) this.archiverManager.getArchiver("WaybackMachine")).updatePendingJobs();
        } catch (ArchiverException e) {
            view.printMessage(e);
            System.out.println(e.getCause().getMessage());
        }

        Iterator<PendingWaybackMachineJob> iterator = this.pendingJobs.iterator();

        while (iterator.hasNext()) {
            PendingWaybackMachineJob pendingJob = iterator.next();

            if (pendingJob.getJob().getStatus().equalsIgnoreCase("success")) {
                pendingJob.getFile().addArchivedURL(pendingJob.getExtractedUrl(), "https://web.archive.org/web/" + pendingJob.getJob().getTimestamp() + "/" + pendingJob.getJob().getOriginal_url());
                iterator.remove(); // Safely remove the current element using iterator
            }
        }

    }

    /**
     * prints the pending jobs
     */
    private void showPendingJobs() {
        view.printFormattedMessage("jobs.showPending.info", this.pendingJobs.size());

        for (PendingWaybackMachineJob job : this.pendingJobs) {
            view.printFormattedMessage("jobs.showPending.job", job.getExtractedUrl(), job.getJob().getStatus());
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
                view.printFormattedMessage("file.validated.info", filePath.getFileName().toString() + " (" + mimeType + ")");
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
        }
    }

    /**
     * Processes the contents of a folder path and initializes the folder model with the contents.
     * Deletes any file from the folder model that fails to process due to a FileModelException.
     *
     * @param folderPath the directory path to process
     */
    private void handleFolder(String folderPath) {
        folderModel = new FolderModel(folderPath);
        extractFilesFromFolder();

        Iterator<FileModel> iterator = folderModel.getFiles().iterator();
        while (iterator.hasNext()) {
            FileModel tempFileModel = iterator.next();
            try {
                processFileModel(tempFileModel);
            } catch (FileModelException | IOException e) {
                view.printMessage(e);
                iterator.remove();
            }
        }

        if (folderModel.getFiles().isEmpty()) {
            handleQuit();
        } else {
            this.fileModel = folderModel.getFiles().getFirst();
        }
    }

    /**
     * Processes a single file path, validates it, and initializes the file model.
     *
     * @param filePath the path of the file to process
     * @throws IOException        if an I/O error occurs when reading the file
     * @throws FileModelException if the file model cannot be initialized
     */
    private void handleSingleFile(String filePath) throws IOException {
        Path validatedPath = Paths.get(filePath);
        String mimeType;
        try {
            mimeType = FileValidator.validate(filePath);
            fileModel = new FileModel(validatedPath, mimeType);
            view.printFormattedMessage("file.validated.info", fileModel.getFileName() + " (" + mimeType + ")");
            processFileModel(fileModel);
        } catch (FileModelException e) {
            view.printMessage(e);
            handleQuit();
        }
    }

    /**
     * Processes the specified file model by reading its content, extracting URLs, and updating the model with these URLs.
     * Throws FileModelException if no URLs are found in the file.
     *
     * @param fileModel The file model representing the file to be processed. It contains the file's path and MIME type.
     * @throws IOException        If an error occurs during reading of the file's content, indicating an I/O problem.
     * @throws FileModelException If no URLs are found in the file.
     */
    private void processFileModel(FileModel fileModel) throws IOException, FileModelException {
        FileReaderInterface fileReader = FileReaderFactory.getFileReader(fileModel.getMimeType());
        String fileContent = fileReader.readFile(fileModel.getFilePath());
        Set<String> extractedURLs = URLExtractor.extractURLs(fileContent);
        if (extractedURLs.isEmpty()) {
            throw new FileModelException(I18n.getString("file.noUrls.error") + " " + fileModel.getFilePath());
        }
        fileModel.addExtractedURLs(extractedURLs);
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
