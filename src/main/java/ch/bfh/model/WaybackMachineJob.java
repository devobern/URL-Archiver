package ch.bfh.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * dataclass for objectmapping the response of get status of job request (wayback machine)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WaybackMachineJob {
    private String job_id;
    private String original_url;
    private String status;
    private String timestamp;

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
}
