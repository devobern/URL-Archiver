package ch.bfh.archiver;

import java.util.List;

/**
 * Encapsulates the result of an archiving operation, containing the archived URLs
 * and a list of archivers that were unavailable during the operation.
 */
public class ArchiverResult {
    private final List<String> archivedUrls;
    private final List<String> unavailableArchivers;

    /**
     * Constructs an ArchiverResult with the given lists of archived URLs and unavailable archivers.
     *
     * @param archivedUrls         A list of strings representing the archived URLs.
     * @param unavailableArchivers A list of strings representing the names of unavailable archivers.
     */
    public ArchiverResult(List<String> archivedUrls, List<String> unavailableArchivers) {
        this.archivedUrls = archivedUrls;
        this.unavailableArchivers = unavailableArchivers;
    }

    /**
     * Retrieves the list of archived URLs.
     *
     * @return A list of strings representing the archived URLs.
     */
    public List<String> getArchivedUrls() {
        return archivedUrls;
    }

    /**
     * Retrieves the list of unavailable archivers.
     *
     * @return A list of strings representing the names of unavailable archivers.
     */
    public List<String> getUnavailableArchivers() {
        return unavailableArchivers;
    }
}
