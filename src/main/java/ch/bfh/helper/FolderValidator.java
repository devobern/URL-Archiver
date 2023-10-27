package ch.bfh.helper;

import ch.bfh.exceptions.PathValidationException;

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
            throw new PathValidationException(I18n.getString("path.invalid.error"));
        }

        if (!Files.isDirectory(path)) {
            throw new PathValidationException(I18n.getString("path.isNotDirectory.error"));
        }

    }
}
