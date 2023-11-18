package ch.bfh.archiver;

/**
 * An implementation of the URLArchiver interface for archiving URLs using the Wayback Machine service.
 * This class encapsulates the functionality specific to the Wayback Machine for archiving purposes.
 */
public class WaybackMachineArchiver implements URLArchiver{
    private final String serviceName = "WaybackMachine";

    /**
     * Archives the given URL using the Wayback Machine service.
     *
     * @param url The URL to be archived.
     * @return A string representing the archived URL, or null if the archiving operation fails.
     */
    @Override
    public String archiveURL(String url) {
        return null;
    }

    /**
     * Checks whether the Wayback Machine service is currently available for use.
     *
     * @return true if the service is available, false otherwise.
     */
    @Override
    public boolean isAvailable() {
        return false;
    }

    /**
     * Retrieves the name of the archiving service provided by this class.
     *
     * @return A string representing the name of the Wayback Machine service.
     */
    @Override
    public String getServiceName() {
        return serviceName;
    }
}
