package ch.bfh.model.archiving;

import ch.bfh.model.FileModel;

public class PendingWaybackMachineJob {
    private final String extractedUrl;
    private WaybackMachineJob job;
    private final FileModel file;

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
