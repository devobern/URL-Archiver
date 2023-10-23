package ch.bfh.handler;

import ch.bfh.ui.ConsoleUI;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Responsible for processing paths and determining their type (file or directory).
 * Outputs relevant information through the {@code ConsoleUI}.
 */
public class PathProcessor {

    private final ConsoleUI consoleUI;

    /**
     * Initializes a new instance of the {@code PathProcessor} class.
     *
     * @param consoleUI The user interface for providing feedback and messages.
     */
    public PathProcessor(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    /**
     * Processes the provided path to determine if it's a directory or a file.
     * Outputs the appropriate message through the {@code ConsoleUI}.
     *
     * @param inputPath The path to be processed.
     */
    public void process(String inputPath) {
        if (Files.isDirectory(Path.of(inputPath))) {
            consoleUI.printFormattedMessage("info.processing_dir");
        } else {
            consoleUI.printFormattedMessage("info.processing_file");
        }

        // Proceed with further processing if the path is valid
    }
}