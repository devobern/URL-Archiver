package ch.bfh.exceptions;

/**
 * Custom exception to handle invalid unicode file formats cases
 */

public class UnicodeFileHandlerException extends Exception {
     /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public UnicodeFileHandlerException(String message) {
        super(message);
    }
}
