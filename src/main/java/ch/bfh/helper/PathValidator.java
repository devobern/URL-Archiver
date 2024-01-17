package ch.bfh.helper;

import ch.bfh.exceptions.PathValidationException;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for validating file system paths.
 * This class provides methods to validate the existence and accessibility of a path.
 */
public class PathValidator {

    /**
     * Validates the given path to ensure it exists, is readable, and is not empty.
     * If the path does not meet these criteria, a {@link PathValidationException} is thrown.
     *
     * @param inputPath the path to be validated as a String.
     * @throws PathValidationException if the path is invalid, does not exist, or is not readable.
     */
    public static void validate(String inputPath) throws PathValidationException {
        String trimmedPath = getTrimmedPath(inputPath);
        Path path = createPath(trimmedPath);
        checkPathExistsAndReadable(path);
    }

    /**
     * Checks if the given path represents a directory.
     * This method does not throw an exception for an invalid path, instead returns false.
     *
     * @param inputPath the path to be checked as a String.
     * @return {@code true} if the path is a directory, {@code false} otherwise.
     */
    public static boolean isFolder(String inputPath) {
        try {
            Path path = createPath(inputPath.trim());
            return Files.isDirectory(path);
        } catch (InvalidPathException | PathValidationException e) {
            return false;
        }
    }

    /**
     * Trims the input path and checks if it's not null or empty.
     * Throws an exception if the path is null or empty.
     *
     * @param inputPath the input path as a String.
     * @return a trimmed version of the input path.
     * @throws PathValidationException if the path is null or empty.
     */
    private static String getTrimmedPath(String inputPath) throws PathValidationException {
        if (inputPath == null || inputPath.trim().isEmpty()) {
            throw new PathValidationException(I18n.getString("path.empty.error"));
        }
        return inputPath.trim();
    }

    /**
     * Converts the given string into a {@link Path} object.
     * Throws an exception if the path string is invalid.
     *
     * @param inputPath the path as a String to be converted.
     * @return the converted Path object.
     * @throws PathValidationException if the path string is invalid.
     */
    private static Path createPath(String inputPath) throws PathValidationException {
        try {
            return Paths.get(inputPath);
        } catch (InvalidPathException e) {
            throw new PathValidationException(I18n.getString("path.invalid.error"));
        }
    }

    /**
     * Checks if the provided Path exists and is readable.
     * Throws an exception if the path does not exist or is not readable.
     *
     * @param path the Path to be checked.
     * @throws PathValidationException if the path does not exist or is not readable.
     */
    private static void checkPathExistsAndReadable(Path path) throws PathValidationException {
        if (!Files.exists(path)) {
            throw new PathValidationException(I18n.getString("path.notExist.error"));
        }
        if (!Files.isReadable(path)) {
            throw new PathValidationException(I18n.getString("path.notReadable.error"));
        }
    }
}
