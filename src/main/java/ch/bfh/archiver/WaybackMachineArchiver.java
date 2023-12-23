package ch.bfh.archiver;

import ch.bfh.controller.CLIController;
import ch.bfh.exceptions.ArchiverException;
import ch.bfh.model.ConfigModel;
import ch.bfh.model.archiving.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;



/**
 * An implementation of the URLArchiver interface for archiving URLs using the Wayback Machine service.
 * This class encapsulates the functionality specific to the Wayback Machine for archiving purposes.
 */
public class WaybackMachineArchiver implements URLArchiver{
    private final String serviceName = "WaybackMachine";
    private final String apiUrl = "https://web.archive.org/save/";
    private final ConfigModel config;
    private final CLIController controller;


    public WaybackMachineArchiver(ConfigModel config, CLIController controller) {
        this.config = config;
        this.controller = controller;
    }

    /**
     * Archives the given URL using the Wayback Machine service.
     *
     * @param url The URL to be archived.
     * @return A string representing the archived URL, or null if the archiving operation fails.
     */
    @Override
    public String archiveURL(String url) throws ArchiverException {
        try {
            // The data to be sent in the request body
            String postData = "url=" + url + "&capture_all=1&skip_first_archive=1";

            // The API key for authorization
            String apiKey = "LOW " + this.config.getAccessKey() + ":" + this.config.getSecretKey();

            // Create an HttpClient
            HttpClient httpClient = HttpClient.newHttpClient();

            // Create a HttpRequest with the necessary headers and data
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Accept", "application/json")
                    .header("Authorization", apiKey)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(postData))
                    .build();

            // Send the request and retrieve the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Get the response code and body
            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode > 299) {
                throw new ArchiverException("Wayback Machine Response is not good: " + responseBody);
            }

            WaybackMachineArchiveResponse archiveResponse = new ObjectMapper().readValue(responseBody, WaybackMachineArchiveResponse.class);

            WaybackMachineJob job = getWaybackMachineJob(archiveResponse.getJob_id());

            if (job.getStatus().contains("error")) {
                throw new ArchiverException("Wayback Machine Website threw an exception: " + job.getException());
            }

            this.controller.addPendingJob(new PendingWaybackMachineJob(url, job, this.controller.getFileModel()));

            return "pending";

        } catch (IOException e) {
            throw new ArchiverException("IO error occurred while archiving URL: " + url, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ArchiverException("The archiving operation was interrupted", e);
        }
    }

    /**
     * Checks whether the Wayback Machine service is currently available for use.
     *
     * @return true if the service is available, false otherwise.
     */
    @Override
    public boolean isAvailable() {
        try {
            // The API key for authorization
            String apiKey = "LOW " + this.config.getAccessKey() + ":" + this.config.getSecretKey();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(
                            URI.create(apiUrl + "status/system"))
                    .header("accept", "application/json")
                    .header("Authorization", apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 300) {
                return true;
            }

        } catch (IOException | InterruptedException e) {
            return false;
        }

        return false;
    }

    /**
     * Retrieves the name of the archiving service provided by this class.
     *
     * @return A string representing the name of the Wayback Machine service.
     */
    @Override
    public String getServiceName() {
        return serviceName;
    }


    /**
     * get the job from the wayback machine
     * @param jobId identifier for a wayback machine job (received from the  wayback machine save response body)
     * @return a WaybackMachineJobe
     */
    private WaybackMachineJob getWaybackMachineJob(String jobId) throws IOException, InterruptedException {

        // The API key for authorization
        String apiKey = "LOW " + this.config.getAccessKey() + ":" + this.config.getSecretKey();

        // Create an HttpClient
        // todo: Implement IDE hint
        HttpClient httpClient = HttpClient.newHttpClient();

        // Create a HttpRequest with the necessary headers and data
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "status/" + jobId))
                .header("Accept", "application/json")
                .header("Authorization", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET()
                .build();

        // Send the request and retrieve the response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return new ObjectMapper().readValue(response.body(), WaybackMachineJob.class);
    }

    /**
     * checks the status of each pending job with the wayback machine and updates the status
     * @throws ArchiverException
     */
    public void updatePendingJobs() throws ArchiverException {

        for (PendingWaybackMachineJob job : this.controller.getPendingJobs()) {
            if (job.getJob().getStatus().equalsIgnoreCase("pending")) {
                try {
                    job.setJob(getWaybackMachineJob(job.getJob().getJob_id()));
                } catch (IOException e) {
                    throw new ArchiverException("IO error occurred while getting Job: " + job.getJob().getJob_id(), e);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ArchiverException("The archiving operation was interrupted", e);
                }

            }
        }
    }


}

