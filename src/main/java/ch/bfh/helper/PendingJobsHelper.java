package ch.bfh.helper;


import ch.bfh.exceptions.PendingJobsException;
import ch.bfh.model.archiving.PendingJobMapper;
import ch.bfh.model.archiving.PendingWaybackMachineJob;
import ch.bfh.model.archiving.WaybackMachineJob;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Helper class for managing pending jobs, specifically for handling
 * serialization and deserialization of pending jobs to and from a JSON file.
 */
public class PendingJobsHelper {
    private static final String DEFAULT_PENDING_JOBS_FILE_PATH = "src/main/resources/pending_jobs.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static String pendingJobsFilepath = DEFAULT_PENDING_JOBS_FILE_PATH;


    /**
     * Sets custom or default file path for pending jobs.
     *
     * @param filePath Custom file path; defaults if null or blank.
     */
    public static void setConfigFilePath(String filePath) {
        pendingJobsFilepath = filePath != null && !filePath.isBlank() ? filePath : DEFAULT_PENDING_JOBS_FILE_PATH;
    }

    /**
     * Saves pending jobs to a file. Overwrites the file if it already exists.
     *
     * @param pendingJobs List of jobs to save.
     * @throws PendingJobsException If file writing fails.
     */
    public static void save(ArrayList<PendingWaybackMachineJob> pendingJobs) throws PendingJobsException {
        File pendingJobsFile = new File(pendingJobsFilepath);

        try {
            // Write the pending jobs to the file. It overwrites if the file already exists.
            OBJECT_MAPPER.writeValue(pendingJobsFile, mapToPendingJobMapper(pendingJobs));
        } catch (IOException e) {
            throw new PendingJobsException("Error writing pending jobs in file: " + e.getMessage());
        }

        if (pendingJobs.isEmpty()) {
            deletePendingJobsFile(pendingJobsFile);
        }
    }

    /**
     * Checks if there are pending jobs stored.
     *
     * @return true if pending jobs exist, false otherwise.
     */
    public static boolean existPendingJobs() {
        try {
            getPendingJobsFile();
        } catch (PendingJobsException e) {
            return false;
        }
        return true;
    }

    /**
     * Reads pending jobs from a file.
     *
     * @return List of read jobs.
     * @throws PendingJobsException If file reading fails.
     */
    public static ArrayList<PendingWaybackMachineJob> read() throws PendingJobsException {
        ArrayList<PendingJobMapper> pendingJobsMapper = getPendingJobsMapper();
        return mapToPendingWaybackMachineJob(pendingJobsMapper);
    }

    /**
     * Retrieves pending jobs data from the file.
     *
     * @return ArrayList of PendingJobMapper.
     * @throws PendingJobsException If reading from file fails.
     */
    private static ArrayList<PendingJobMapper> getPendingJobsMapper() throws PendingJobsException {
        try {
            return OBJECT_MAPPER.readValue(getPendingJobsFile(), new TypeReference<ArrayList<PendingJobMapper>>() {
            });
        } catch (IOException e) {
            throw new PendingJobsException("Error reading pending jobs: " + e.getMessage());
        }
    }

    /**
     * Gets the file for pending jobs.
     *
     * @return File object for the pending jobs.
     * @throws PendingJobsException If file is not found or is a directory.
     */
    private static File getPendingJobsFile() throws PendingJobsException {
        File pendingJobsFile = new File(pendingJobsFilepath);

        if (!pendingJobsFile.exists() || pendingJobsFile.isDirectory()) {
            throw new PendingJobsException("Pending jobs file does not exist or is a directory: " + pendingJobsFilepath);
        }

        return pendingJobsFile;
    }

    /**
     * Converts PendingWaybackMachineJob list to PendingJobMapper list.
     *
     * @param pendingJobs List of PendingWaybackMachineJob.
     * @return Mapped list of PendingJobMapper.
     */
    private static ArrayList<PendingJobMapper> mapToPendingJobMapper(ArrayList<PendingWaybackMachineJob> pendingJobs) {
        ArrayList<PendingJobMapper> pendingJobsMapper = new ArrayList<>();
        for (PendingWaybackMachineJob pendingJob : pendingJobs) {
            pendingJobsMapper.add(new PendingJobMapper(pendingJob.getExtractedUrl(), pendingJob.getJob().getJob_id()));
        }

        return pendingJobsMapper;
    }

    /**
     * Converts PendingJobMapper list to PendingWaybackMachineJob list.
     *
     * @param pendingJobsMapper List of PendingJobMapper.
     * @return Mapped list of PendingWaybackMachineJob.
     */
    private static ArrayList<PendingWaybackMachineJob> mapToPendingWaybackMachineJob(ArrayList<PendingJobMapper> pendingJobsMapper) {
        ArrayList<PendingWaybackMachineJob> pendingJobs = new ArrayList<>();

        for (PendingJobMapper jobMapper : pendingJobsMapper) {
            WaybackMachineJob job = new WaybackMachineJob();
            job.setJob_id(jobMapper.getJob_id());
            pendingJobs.add(new PendingWaybackMachineJob(
                    jobMapper.getExtractedUrl(),
                    job
            ));
        }

        return pendingJobs;
    }

    /**
     * Deletes the pending jobs file.
     *
     * @param file File object representing the pending jobs file.
     * @throws PendingJobsException If file deletion fails.
     */
    private static void deletePendingJobsFile(File file) throws PendingJobsException {
        if (!file.delete()) {
            throw new PendingJobsException("Couldn't delete the pending jobs file");
        }
    }


}
