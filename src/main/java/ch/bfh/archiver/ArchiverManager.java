package ch.bfh.archiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages a collection of URL archivers and provides functionality
 * to archive URLs using selected archiving services.
 */
public class ArchiverManager {
    private Map<String, URLArchiver> archivers = new HashMap<>();

    /**
     * Adds an archiver to the manager using its service name as the key.
     *
     * @param archiver the URLArchiver instance to be added
     */
    public void addArchiver(URLArchiver archiver) {
        String serviceName = archiver.getServiceName();
        archivers.put(serviceName, archiver);
    }

    /**
     * Retrieves an archiver by its name identifier.
     *
     * @param name the name identifier of the archiver to retrieve
     * @return the URLArchiver instance associated with the given name
     */
    public URLArchiver getArchiver(String name) {
        return archivers.get(name);
    }

    /**
     * Returns a list of all archivers managed by this manager.
     *
     * @return a list containing all URLArchiver instances
     */
    public List<URLArchiver> getAllArchivers() {
        return new ArrayList<>(archivers.values());
    }

    /**
     * Archives a URL using the selected archivers.
     *
     * @param url               the URL to be archived
     * @param selectedArchivers a list of selected URLArchiver instances to perform the archiving
     * @return an ArchiverResult object containing the archived URLs and any unavailable archivers
     */
    public ArchiverResult archive(String url, List<URLArchiver> selectedArchivers) {
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
