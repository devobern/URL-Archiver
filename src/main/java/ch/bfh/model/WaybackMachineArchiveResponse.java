package ch.bfh.model;

public class WaybackMaschineArchiveResponse {
    private String url;
    private String job_id;
    private String message;

    public String getUrl() {
        return url;
    }

    public String getJob_id() {
        return job_id;
    }

    public String getMessage() {
        return message;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
