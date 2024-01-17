package ch.bfh.model.archiving;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents the status response of a job request from the Wayback Machine.
 * This class is used for mapping the JSON response to Java fields.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WaybackMachineJob {
    private String job_id;
    private String original_url;
    private String status;
    private String timestamp;
    private String exception;

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getOriginal_url() {
        return original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
