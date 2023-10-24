package ch.bfh.validator;

import ch.bfh.exceptions.PathValidationException;
import ch.bfh.exceptions.UnicodeFileFormatException;
import ch.bfh.ui.ConsoleUI;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderValidator {
    public void validate(String inputPath) throws PathValidationException {
        Path path;
        try {
            // Use trim() to remove accidentally added leading and trailing
            // whitespaces for better user experience.
            path = Paths.get(inputPath.trim());
        } catch (InvalidPathException e) {
            throw new PathValidationException(ConsoleUI.messages.getString("path.invalid.error"));
        }

        if (!Files.isDirectory(path)) {
            throw new PathValidationException(ConsoleUI.messages.getString("path.isNotDirectory.error"));
        }

    }
}
