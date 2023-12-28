package ch.bfh.model.archiving;

import ch.bfh.model.FileModel;

/**
 * Represents a pending archiving job for the Wayback Machine.
 * It holds information about the extracted URL, the job itself, and the associated file.
 */
public class PendingWaybackMachineJob {
    private final String extractedUrl;
    private final FileModel file;
    private WaybackMachineJob job;

    /**
     * Constructs a new instance of PendingWaybackMachineJob.
     *
     * @param extractedUrl The URL extracted for archiving.
     * @param job          The Wayback Machine job details.
     * @param file         The file model associated with this archiving job.
     */
    public PendingWaybackMachineJob(String extractedUrl, WaybackMachineJob job, FileModel file) {
        this.extractedUrl = extractedUrl;
        this.job = job;
        this.file = file;
    }

    public String getExtractedUrl() {
        return extractedUrl;
    }

    public WaybackMachineJob getJob() {
        return job;
    }

    public void setJob(WaybackMachineJob job) {
        this.job = job;
    }

    public FileModel getFile() {
        return file;
    }
}
