package ch.bfh.model.archiving;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents the response received from a Wayback Machine save website request.
 * This class maps the JSON response to Java fields.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WaybackMachineArchiveResponse {
    private String url;
    private String job_id;
    private String message;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
