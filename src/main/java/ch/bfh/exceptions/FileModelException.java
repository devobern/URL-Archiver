package ch.bfh.exceptions;

/**
 * Custom exception to handle invalid unicode file formats cases
 */

public class FileModelException extends Exception {
     /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public FileModelException(String message) {
        super(message);
    }
}
