package ch.bfh.exceptions;

/**
 * Custom exception to handle invalid unicode file formats cases
 */

public class FolderHandlerException extends Exception {
     /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public FolderHandlerException(String message) {
        super(message);
    }
}