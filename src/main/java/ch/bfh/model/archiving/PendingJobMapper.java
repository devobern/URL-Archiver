package ch.bfh.model.archiving;

public class PendingJobMapper {
    private String extractedUrl;
    private String job_id;

    public PendingJobMapper(String extractedUrl, String job_id) {
        this.extractedUrl = extractedUrl;
        this.job_id = job_id;
    }

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
