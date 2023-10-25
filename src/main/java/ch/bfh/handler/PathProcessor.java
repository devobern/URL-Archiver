package ch.bfh.handler;

import ch.bfh.view.ConsoleView;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Responsible for processing paths and determining their type (file or directory).
 * Outputs relevant information through the {@code ConsoleUI}.
 */
public class PathProcessor {

    private final ConsoleView consoleView;

    /**
     * Initializes a new instance of the {@code PathProcessor} class.
     *
     * @param consoleView The user interface for providing feedback and messages.
     */
    public PathProcessor(ConsoleView consoleView) {
        this.consoleView = consoleView;
    }

    /**
     * Processes the provided path to determine if it's a directory or a file.
     * Outputs the appropriate message through the {@code ConsoleUI}.
     *
     * @param inputPath The path to be processed.
     */
    public void process(String inputPath) {
        if (Files.isDirectory(Path.of(inputPath))) {
            consoleView.printFormattedMessage("info.processing_dir");
        } else {
            consoleView.printFormattedMessage("info.processing_file");
        }

        // Proceed with further processing if the path is valid
    }
}