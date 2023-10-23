package ch.bfh.handler;

/**
 * Handles command line input arguments and delegates the processing
 * to the appropriate input handler.
 */
public class CommandLineHandler {
    private final FileInputHandler fileInputHandler;

    /**
     * Initializes a new instance of the {@code CommandLineHandler} class.
     *
     * @param fileInputHandler The handler to process file input operations.
     */
    public CommandLineHandler(FileInputHandler fileInputHandler) {
        this.fileInputHandler = fileInputHandler;
    }

    /**
     * Handles the provided command line arguments.
     * Delegates to the {@code FileInputHandler} for further processing.
     *
     * @param args The command line arguments provided during application launch.
     */
    public void handleArgs(String[] args) {
        if (args.length > 0) {
            fileInputHandler.processCommandLineInput(args[0]);
        } else {
            fileInputHandler.promptUserForInput();
        }
    }
}