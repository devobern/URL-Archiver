package ch.bfh.archiver;

import ch.bfh.controller.CLIController;
import ch.bfh.exceptions.ArchiverException;
import ch.bfh.model.ConfigModel;
import ch.bfh.model.archiving.PendingWaybackMachineJob;
import ch.bfh.model.archiving.WaybackMachineArchiveResponse;
import ch.bfh.model.archiving.WaybackMachineJob;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Archiver implementation for the Wayback Machine service.
 * This class handles the process of archiving URLs using the Wayback Machine API.
 */
public class WaybackMachineArchiver implements URLArchiver {
    private static final String SERVICE_NAME = "WaybackMachine";
    private final boolean automated = true;
    private static final String API_URL = "https://web.archive.org/save/";
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private final ConfigModel config;
    private final CLIController controller;

    /**
     * Constructs a new WaybackMachineArchiver.
     *
     * @param config     Configuration model for the archiver.
     * @param controller Controller for managing CLI interactions.
     */
    public WaybackMachineArchiver(ConfigModel config, CLIController controller) {
        this.config = config;
        this.controller = controller;
    }

    /**
     * Archives a given URL using the Wayback Machine service.
     *
     * @param url URL to be archived.
     * @return The status of the archiving operation.
     * @throws ArchiverException if an error occurs during the archiving process.
     */
    @Override
    public String archiveURL(String url) throws ArchiverException {
        try {
            String postData = "url=" + url + "&capture_all=1&skip_first_archive=1";
            HttpRequest request = createPostRequest(API_URL, postData);
            HttpResponse<String> response = sendRequest(request);

            validateResponse(response);

            WaybackMachineArchiveResponse archiveResponse = new ObjectMapper().readValue(response.body(), WaybackMachineArchiveResponse.class);
            WaybackMachineJob job = getWaybackMachineJob(archiveResponse.getJob_id());

            validateJobStatus(job);

            this.controller.addPendingJob(new PendingWaybackMachineJob(url, job, this.controller.getFileModel()));

            return "pending";

        } catch (IOException e) {
            if (e.getMessage() != null) {
                throw new ArchiverException("IO error occurred while archiving URL: " + url, e);
            }
            return "pending";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ArchiverException("The archiving operation was interrupted", e);
        }
    }

    /**
     * Checks if the Wayback Machine service is available.
     *
     * @return true if the service is available, false otherwise.
     */
    @Override
    public boolean isAvailable() {
        try {
            HttpRequest request = createGetRequest(API_URL + "status/system");
            HttpResponse<String> response = sendRequest(request);
            return response.statusCode() < 300;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }


    /**
     * Retrieves the name of the Wayback Machine service.
     *
     * @return The name of the service.
     */
    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    /**
     * Checks if the archiving service is automated.
     *
     * @return true if the service is automated, false otherwise.
     */
    @Override
    public boolean isAutomated() {
        return automated;
    }

    /**
     * Retrieves the details of a Wayback Machine job using the provided job ID.
     *
     * @param jobId The job ID to query.
     * @return The retrieved Wayback Machine job details.
     * @throws IOException          if a network-related exception occurs.
     * @throws InterruptedException if the thread is interrupted while waiting for the response.
     */
    private WaybackMachineJob getWaybackMachineJob(String jobId) throws IOException, InterruptedException {
        HttpRequest request = createGetRequest(API_URL + "status/" + jobId);
        HttpResponse<String> response = sendRequest(request);
        return new ObjectMapper().readValue(response.body(), WaybackMachineJob.class);
    }

    /**
     * Updates the status of all pending jobs.
     *
     * @throws ArchiverException if an error occurs while updating job statuses.
     */
    public void updatePendingJobs() throws ArchiverException {
        for (PendingWaybackMachineJob job : this.controller.getPendingJobs()) {
            updateJobStatus(job);
        }
    }

    private HttpRequest createPostRequest(String url, String postData) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", APPLICATION_JSON)
                .header("Authorization", getApiKey())
                .header("Content-Type", CONTENT_TYPE)
                .POST(HttpRequest.BodyPublishers.ofString(postData))
                .build();
    }

    private HttpRequest createGetRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", APPLICATION_JSON)
                .header("Authorization", getApiKey())
                .GET()
                .build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void validateResponse(HttpResponse<String> response) throws ArchiverException {
        if (response.statusCode() > 299) {
            throw new ArchiverException("Non-successful response: " + response.body());
        }
    }

    private void validateJobStatus(WaybackMachineJob job) throws ArchiverException {
        if (job.getStatus().contains("error")) {
            throw new ArchiverException("Error in Wayback Machine job: " + job.getException());
        }
    }

    private void updateJobStatus(PendingWaybackMachineJob job) throws ArchiverException {
        if (job.getJob().getStatus().equalsIgnoreCase("pending")) {
            try {
                job.setJob(getWaybackMachineJob(job.getJob().getJob_id()));
            } catch (IOException e) {
                if (e.getMessage() != null) {
                    throw new ArchiverException("IO error occurred while getting Job: " + job.getJob().getJob_id(), e);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ArchiverException("The archiving operation was interrupted", e);
            }
        }
    }

    private String getApiKey() {
        return "LOW " + this.config.getAccessKey() + ":" + this.config.getSecretKey();
    }
}
