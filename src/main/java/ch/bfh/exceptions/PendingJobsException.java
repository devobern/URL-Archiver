package ch.bfh.exceptions;

/**
 * Exception class for handling errors related to pending jobs
 */
public class PendingJobsException extends Exception {
    /**
     * Constructs a new PendingJobsException with the specified detail message.
     *
     * @param message The detail message. The detail message is saved for
     *                later retrieval by the {@link Throwable#getMessage()} method.
     */
    public PendingJobsException(String message) {
        super(message);
    }
}
