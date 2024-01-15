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

public class PendingJobsHelper {
    private static final String DEFAULT_PENDING_JOBS_FILE_PATH = "src/main/resources/pending_jobs.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static String pendingJobsFilepath = DEFAULT_PENDING_JOBS_FILE_PATH;


    /**
     * Sets the path for the pending jobs file.
     * @param filePath Custom path for the pending jobs file, falls back to default if null or blank.
     */
    public static void setConfigFilePath(String filePath) {
        pendingJobsFilepath = filePath != null && !filePath.isBlank() ? filePath : DEFAULT_PENDING_JOBS_FILE_PATH;
    }


    /**
     * writes a list of pending jobs into a json file. Overwrites the file if it already exists.
     *
     * @param pendingJobs a list of PendingWaybackMachine jobs to save
     * @throws PendingJobsException if writing the pending jobs file fails
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

    public static boolean existPendingJobs() {
        try {
            getPendingJobsFile();
        } catch (PendingJobsException e) {
            return false;
        }
        return true;
    }

    public static ArrayList<PendingWaybackMachineJob> read() throws PendingJobsException {
        ArrayList<PendingJobMapper> pendingJobsMapper = getPendingJobsMapper();
        return mapToPendingWaybackMachineJob(pendingJobsMapper);
    }

    private static ArrayList<PendingJobMapper> getPendingJobsMapper() throws PendingJobsException {
        try {
            return OBJECT_MAPPER.readValue(getPendingJobsFile(), new TypeReference<ArrayList<PendingJobMapper>>() {});
        } catch (IOException e) {
            throw new PendingJobsException("Error reading pending jobs: " + e.getMessage());
        }
    }

    private static File getPendingJobsFile() throws PendingJobsException {
        File pendingJobsFile = new File(pendingJobsFilepath);

        if (!pendingJobsFile.exists() || pendingJobsFile.isDirectory()) {
            throw new PendingJobsException("Pending jobs file does not exist or is a directory: " + pendingJobsFilepath);
        }

        return pendingJobsFile;
    }

    private static ArrayList<PendingJobMapper> mapToPendingJobMapper(ArrayList<PendingWaybackMachineJob> pendingJobs) {
        ArrayList<PendingJobMapper> pendingJobsMapper = new ArrayList<>();
        for (PendingWaybackMachineJob pendingJob : pendingJobs) {
            pendingJobsMapper.add(new PendingJobMapper(pendingJob.getExtractedUrl(), pendingJob.getJob().getJob_id()));
        }

        return pendingJobsMapper;
    }

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

    private static void deletePendingJobsFile(File file) throws PendingJobsException {
        if(!file.delete()) {
            throw new PendingJobsException("Couldn't delete the pending jobs file");
        }
    }



}
