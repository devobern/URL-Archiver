package ch.bfh.exceptions;

/**
 * Custom exception to handle invalid unicode file formats cases
 */

public class UnicodeFileFormatException extends Exception {
     /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public UnicodeFileFormatException(String message) {
        super(message);
    }
}
