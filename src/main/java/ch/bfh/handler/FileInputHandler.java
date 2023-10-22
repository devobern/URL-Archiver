package ch.bfh.handler;

import ch.bfh.ui.ConsoleUI;
import ch.bfh.validator.PathValidator;
import ch.bfh.exceptions.PathValidationException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class FileInputHandler {

    /**
     * Prompts the user to provide a file or directory path.
     * Repeatedly asks until a valid path is given.
     */
    public void promptUserForInput() {
        Scanner scanner = new Scanner(System.in);
        boolean isVlaid = false;
        while(!isVlaid) {
            ConsoleUI.printMessage("path.prompt");
            String path = scanner.nextLine();
            try {
                PathValidator.validate(path);
                processPath(path);
                isVlaid = true;
            } catch (PathValidationException e) {
                System.out.println(e.getMessage());
                ConsoleUI.printFormattedMessage("error.retry");
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
        ConsoleUI.printFormattedMessage("info.command_line_arg");
        try {
            PathValidator.validate(path);
            processPath(path);
        } catch (PathValidationException e) {
            System.out.println(e.getMessage());
            ConsoleUI.printFormattedMessage("error.retry");
            promptUserForInput();
        }
    }

    /**
     * Processes the given file or directory path.
     * Determines if it's a file or directory and displays an appropriate message.
     *
     * @param inputPath the path to be processed
     */
    private void processPath(String inputPath) {
        if(Files.isDirectory(Path.of(inputPath))){
            ConsoleUI.printFormattedMessage("info.processing_dir");
        } else {
            ConsoleUI.printFormattedMessage("info.processing_file");
        }

        // Proceed with further processing if path is valid
    }
}
