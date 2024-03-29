package ch.bfh.archiver;

import ch.bfh.exceptions.ArchiverException;

/**
 * Defines the contract for archiving services capable of archiving URLs.
 * Implementations of this interface should provide the specific mechanics
 * for archiving URLs with different archiving services.
 */
public interface URLArchiver {
    /**
     * Archives the specified URL.
     *
     * @param url The URL to archive.
     * @return The archived URL as a string, or null if archiving is unsuccessful.
     */
    String archiveURL(String url) throws ArchiverException;

    /**
     * Checks if the archiving service is currently available.
     *
     * @return true if the service is available, false otherwise.
     */
    boolean isAvailable();

    /**
     * Gets the name of the archiving service.
     *
     * @return The name of the service.
     */
    String getServiceName();

    /**
     * Checks if the archiving service is automated.
     *
     * @return true if the service is automated, false otherwise.
     */
    boolean isAutomated();
    // todo: Maybe we can add more methods
}

