package ch.bfh.archiver;

/**
 * Implementation of the URLArchiver interface for the Archive.today service.
 * This class provides the mechanism to archive URLs using Archive.today's archiving capabilities.
 */
public class ArchiveTodayArchiver implements URLArchiver{

    private final String serviceName = "ArchiveToday";

    /**
     * Archives the given URL using the Archive.today service.
     *
     * @param url The URL to be archived.
     * @return A string representing the archived URL, or null if the service is not available.
     */
    @Override
    public String archiveURL(String url) {
        return null;
    }

    /**
     * Checks whether the Archive.today service is available for use.
     *
     * @return true if the service is available, false otherwise.
     */
    @Override
    public boolean isAvailable() {
        return false;
    }

    /**
     * Returns the name of the archiving service.
     *
     * @return A string representing the name of the service.
     */
    @Override
    public String getServiceName() {
        return serviceName;
    }
}
