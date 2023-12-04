package ch.bfh.archiver;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Encapsulates the result of an archiving operation, containing the archived URLs
 * and a list of archivers that were unavailable during the operation.
 */
public record ArchiverResult(List<String> archivedUrls, List<String> unavailableArchivers) {
    /**
     * Constructs an ArchiverResult with the given lists of archived URLs and unavailable archivers.
     *
     * @param archivedUrls         A list of strings representing the archived URLs.
     * @param unavailableArchivers A list of strings representing the names of unavailable archivers.
     */
    public ArchiverResult(List<String> archivedUrls, List<String> unavailableArchivers) {
        this.archivedUrls = Collections.unmodifiableList(Objects.requireNonNull(archivedUrls, "archivedUrls must not be null"));
        this.unavailableArchivers = Collections.unmodifiableList(Objects.requireNonNull(unavailableArchivers, "unavailableArchivers must not be null"));
    }

    /**
     * Retrieves the list of archived URLs.
     *
     * @return A list of strings representing the archived URLs.
     */
    @Override
    public List<String> archivedUrls() {
        return archivedUrls;
    }

    /**
     * Retrieves the list of unavailable archivers.
     *
     * @return A list of strings representing the names of unavailable archivers.
     */
    @Override
    public List<String> unavailableArchivers() {
        return unavailableArchivers;
    }
}
