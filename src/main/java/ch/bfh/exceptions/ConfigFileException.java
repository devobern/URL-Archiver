package ch.bfh.exceptions;

/**
 * Exception class for handling errors related to configuration file operations.
 */
public class ConfigFileException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message explaining the exception.
     */
    public ConfigFileException(String message) {
        super(message);
    }
}
