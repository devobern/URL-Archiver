package ch.bfh.archiver;

import ch.bfh.exceptions.ArchiverException;
import ch.bfh.model.ConfigModel;
import ch.bfh.model.WaybackMachineArchiveResponse;
import ch.bfh.model.WaybackMachineJob;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
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
    private ConfigModel config;

    public WaybackMachineArchiver(ConfigModel config) {
        this.config = config;
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
            String postData = "url=" + url + "&capture_all=1";

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

            WaybackMachineArchiveResponse archiveResponse = new ObjectMapper().readValue(responseBody, WaybackMachineArchiveResponse.class);

            WaybackMachineJob job = waitForJob(archiveResponse);

            if (job.getStatus().contains("error")) {
                throw new ArchiverException("Wayback Machine Website threw an exception: " + job.getException());
            }

            String archivedUrl = "https://web.archive.org/web/" + job.getTimestamp() + "/" + job.getOriginal_url();

            return archivedUrl;


        } catch (IOException e) {
            System.out.println(e);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        return null;
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

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 300) {
                return true;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
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
     * method for waiting till the archiving job is finished
     * @param archiveResponse the response from the archiving request --> contains the job id
     * @return returns the successful job --> contains the information to the archived urlAutomated URL Submission Wayback Machine
     * @throws IOException
     * @throws InterruptedException
     */
    private WaybackMachineJob waitForJob(WaybackMachineArchiveResponse archiveResponse) throws IOException, InterruptedException {

        // The API key for authorization
        String apiKey = "LOW " + this.config.getAccessKey() + ":" + this.config.getSecretKey();

        // Create an HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Create a HttpRequest with the necessary headers and data
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "status/" + archiveResponse.getJob_id()))
                .header("Accept", "application/json")
                .header("Authorization", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET()
                .build();

        // Send the request and retrieve the response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        WaybackMachineJob job = new ObjectMapper().readValue(response.body(), WaybackMachineJob.class);


        while (job.getStatus().equals("pending")) {

            // Send the request and retrieve the response
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            job = new ObjectMapper().readValue(response.body(), WaybackMachineJob.class);

            Thread.sleep(200);
        }

        return job;
    }
}
