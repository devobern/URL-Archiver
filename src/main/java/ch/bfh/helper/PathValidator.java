package ch.bfh.helper;

import ch.bfh.exceptions.PathValidationException;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class responsible for validating paths. Provides methods to check
 * whether the specified path exists, is readable, and meets other criteria.
 *
 * <p>If a validation fails, a {@code PathValidationException} is thrown, informing
 * the caller about the specific issue with the path.</p>
 */
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
            throw new PathValidationException(I18n.getString("path.empty.error"));
        }
        Path path;
        try {
            // Use trim() to remove accidentally added leading and trailing
            // whitespaces for better user experience.
            path = Paths.get(inputPath.trim());
        } catch (InvalidPathException e) {
            throw new PathValidationException(I18n.getString("path.invalid.error"));
        }

        if (!Files.exists(path)) {
            throw new PathValidationException(I18n.getString("path.notExist.error"));
        }

        if (!Files.isReadable(path)) {
            throw new PathValidationException(I18n.getString("path.notReadable.error"));
        }
    }

    public static boolean isFolder(String inputPath) throws PathValidationException {
        Path path;
        try {
            // Use trim() to remove accidentally added leading and trailing
            // whitespaces for better user experience.
            path = Paths.get(inputPath.trim());
        } catch (InvalidPathException e) {
            throw new PathValidationException(I18n.getString("path.invalid.error"));
        }

        return Files.isDirectory(path);
    }
}

