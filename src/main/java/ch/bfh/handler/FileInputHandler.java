package ch.bfh.handler;

import ch.bfh.view.ConsoleView;
import ch.bfh.helper.PathValidator;
import ch.bfh.exceptions.PathValidationException;

/**
 * Handles the input for files or directory paths, including validation, processing, and user interactions.
 * Works in conjunction with {@code ConsoleUI}, {@code UserInputHandler}, and {@code PathProcessor} to manage the workflow.
 */
public class FileInputHandler {
    private final ConsoleView consoleView;
    private final UserInputHandler userInputHandler;
    private final PathProcessor pathProcessor;

    /**
     * Initializes a new instance of the {@code FileInputHandler} class.
     *
     * @param consoleView The user interface for providing feedback and messages.
     */
    public FileInputHandler(ConsoleView consoleView){
        this.consoleView = consoleView;
        this.userInputHandler = new UserInputHandler(consoleView);
        this.pathProcessor = new PathProcessor(consoleView);
    }

    /**
     * Prompts the user to provide a file or directory path.
     * Repeatedly asks until a valid path is given.
     */
    public void promptUserForInput() {
        boolean isValid = false;
        while(!isValid) {
            String path = userInputHandler.promptUserForPath();
            try {
                PathValidator.validate(path);
                pathProcessor.process(path);
                isValid = true;
            } catch (PathValidationException e) {
                System.out.println(e.getMessage());
                consoleView.printFormattedMessage("error.retry");
            }
        }
    }

    /**
     * Processes the file or directory path provided via command line.
     * If invalid, prompts the user to manually enter a valid path.
     *
     * @param path the path provided via command line
     */
    public void processCommandLineInput(String path) {
        consoleView.printFormattedMessage("info.command_line_arg");
        try {
            PathValidator.validate(path);
            pathProcessor.process(path);
        } catch (PathValidationException e) {
            System.out.println(e.getMessage());
            consoleView.printFormattedMessage("error.retry");
            promptUserForInput();
        }
    }
}
