package ch.bfh.exceptions;

/**
 * Exception class for handling errors related to URL exporting operations.
 */
public class URLExporterException extends Exception {

    /**
     * Constructs a new URLExporterException with the specified detail message.
     *
     * @param message The detail message which is saved for later retrieval
     *                by the {@link Throwable#getMessage()} method.
     */
    public URLExporterException(String message) {
        super(message);
    }
}
