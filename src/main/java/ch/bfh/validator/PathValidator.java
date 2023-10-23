package ch.bfh.validator;

import ch.bfh.exceptions.PathValidationException;
import ch.bfh.ui.ConsoleUI;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathValidator {

    /**
     * Validates the given path to ensure it exists, is readable, and is not empty.
     * Throws a PathValidationException for any validation failures.
     *
     * @param inputPath the path to be validated
     * @throws PathValidationException if the path is invalid
     */
    public static void validate(String inputPath) throws PathValidationException {
        // Use trim() to remove leading and trailing whitespace to ensure
        // that strings consisting solely of spaces are treated as empty.
        if (inputPath == null || inputPath.trim().isEmpty()) {
            throw new PathValidationException(ConsoleUI.messages.getString("path.empty.error"));
        }
        Path path;
        try {
            // Use trim() to remove accidentally added leading and trailing
            // whitespaces for better user experience.
            path = Paths.get(inputPath.trim());
        } catch (InvalidPathException e) {
            throw new PathValidationException(ConsoleUI.messages.getString("path.invalid.error"));
        }

        if (!Files.exists(path)) {
            throw new PathValidationException(ConsoleUI.messages.getString("path.notExist.error"));
        }

        if (!Files.isReadable(path)) {
            throw new PathValidationException(ConsoleUI.messages.getString("path.notReadable.error"));
        }
    }
}