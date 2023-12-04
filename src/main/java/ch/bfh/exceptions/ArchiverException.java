package ch.bfh.exceptions;

public class ArchiverException extends Exception {
    public ArchiverException(String message) {
        super(message);
    }
    // Constructor with both message and cause
    public ArchiverException(String message, Throwable cause) {
        super(message, cause);
    }
}
