package ch.bfh.archiver;

import ch.bfh.exceptions.ArchiverException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages a collection of URL archivers and provides functionality
 * to archive URLs using selected archiving services.
 */
public class ArchiverManager {
    private final Map<String, URLArchiver> archivers = new HashMap<>();

    /**
     * Adds an archiver to the manager using its service name as the key.
     * <p>
     * This method stores the provided {@link URLArchiver} instance in the manager.
     * The service name of the archiver is used as the key for storage, allowing
     * for easy retrieval and management of different archivers.
     * </p>
     *
     * @param archiver the {@link URLArchiver} instance to be added to the manager.
     *                 It should not be null, and should have a unique service name
     *                 to avoid overwriting existing entries.
     */
    public void addArchiver(URLArchiver archiver) {
        String serviceName = archiver.getServiceName();
        archivers.put(serviceName, archiver);
    }

    /**
     * Retrieves an archiver by its service name identifier.
     * <p>
     * This method returns the {@link URLArchiver} instance associated with the given
     * service name. If no archiver is found for the specified name, this method
     * returns {@code null}.
     * </p>
     *
     * @param name the service name identifier of the archiver to retrieve.
     * @return the {@link URLArchiver} instance associated with the given service name,
     *         or {@code null} if no such archiver is found.
     */
    public URLArchiver getArchiver(String name) {
        return archivers.get(name);
    }

    /**
     * Returns a list of all archivers managed by this manager.
     * <p>
     * This method provides access to all {@link URLArchiver} instances currently
     * managed by this ArchiverManager. The returned list is a snapshot of the current
     * state and modifications to this list do not affect the internal state of the
     * manager.
     * </p>
     *
     * @return a list containing all {@link URLArchiver} instances managed by this
     *         manager. If no archivers are managed, an empty list is returned.
     */
    public List<URLArchiver> getAllArchivers() {
        return Collections.unmodifiableList(new ArrayList<>(archivers.values()));
    }

    /**
     * Archives a URL using the selected archivers.
     * <p>
     * This method processes the given URL with each archiver provided in the list of selected archivers.
     * It attempts to archive the URL using each archiver and collects the results. If an archiver is
     * unavailable, it is noted, and its service name is added to a list of unavailable archivers.
     * </p>
     * <p>
     * The method returns an {@link ArchiverResult} object containing two lists:
     * one for successfully archived URLs and another for the names of unavailable archivers.
     * </p>
     *
     * @param url               the URL to be archived.
     * @param selectedArchivers a list of {@link URLArchiver} instances to perform the archiving.
     *                          Each archiver is used to archive the provided URL.
     * @return an {@link ArchiverResult} object containing lists of successfully archived URLs and
     *         unavailable archivers. If an archiver successfully processes the URL, its result is added
     *         to the archived URLs list; if it is unavailable, its name is added to the unavailable
     *         archivers list.
     * @throws ArchiverException if an error occurs during the archiving process. This exception is
     *                           thrown to indicate processing errors specific to individual archivers
     *                           or general issues encountered while archiving.
     */
    public ArchiverResult archive(String url, List<URLArchiver> selectedArchivers) throws ArchiverException {
        List<String> archivedUrls = new ArrayList<>();
        List<String> unavailableArchivers = new ArrayList<>();

        for (URLArchiver archiver : selectedArchivers) {
            if (archiver.isAvailable()) {
                String archivedUrl = archiver.archiveURL(url);
                if (archivedUrl != null && !archivedUrl.isEmpty()) {
                    archivedUrls.add(archivedUrl);
                }
            } else {
                unavailableArchivers.add(archiver.getServiceName());
            }
        }
        return new ArchiverResult(archivedUrls, unavailableArchivers);
    }
}
