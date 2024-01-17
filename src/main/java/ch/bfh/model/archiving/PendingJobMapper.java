package ch.bfh.model.archiving;

/**
 * Represents a mapping for a pending job in the archiving process, associating an extracted URL with a job ID.
 */
public class PendingJobMapper {
    private String extractedUrl;
    private String job_id;

    /**
     * Constructs a new PendingJobMapper with specified URL and job ID.
     *
     * @param extractedUrl The extracted URL to be associated with the job.
     * @param job_id       The unique identifier of the job.
     */
    public PendingJobMapper(String extractedUrl, String job_id) {
        this.extractedUrl = extractedUrl;
        this.job_id = job_id;
    }

    /**
     * Default constructor initializing fields with empty strings.
     */
    public PendingJobMapper() {
        this.extractedUrl = "";
        this.job_id = "";
    }

    public String getExtractedUrl() {
        return extractedUrl;
    }

    public void setExtractedUrl(String extractedUrl) {
        this.extractedUrl = extractedUrl;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }
}
