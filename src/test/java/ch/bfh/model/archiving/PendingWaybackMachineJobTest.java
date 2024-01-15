package ch.bfh.model.archiving;

import ch.bfh.model.FileModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the PendingWaybackMachineJob class.
 */
class PendingWaybackMachineJobTest {

    private PendingWaybackMachineJob pendingJob;
    private WaybackMachineJob job;
    private FileModel file;

    @TempDir
    Path tempDir;

    /**
     * Sets up the testing environment before each test.
     * Initializes a WaybackMachineJob, a FileModel, and a PendingWaybackMachineJob.
     */
    @BeforeEach
    void setUp() {
        job = new WaybackMachineJob();
        file = new FileModel(tempDir.resolve("test.txt"),"text/plain");
        pendingJob = new PendingWaybackMachineJob("http://brewster.kahle.org/", job, file);
    }

    /**
     * Tests if the PendingWaybackMachineJob constructor correctly assigns all fields.
     */
    @Test
    void testConstructor() {
        assertEquals("http://brewster.kahle.org/", pendingJob.getExtractedUrl(), "Extracted URL should match constructor input");
        assertEquals(job, pendingJob.getJob(), "Job should match constructor input");
        assertEquals(file, pendingJob.getFile(), "File should match constructor input");
    }

    /**
     * Tests if the getExtractedUrl method returns the correct URL.
     */
    @Test
    void testGetExtractedUrl() {
        assertEquals("http://brewster.kahle.org/", pendingJob.getExtractedUrl(), "getExtractedUrl should return the expected value");
    }

    /**
     * Tests the functionality of getJob and setJob methods.
     */
    @Test
    void testGetSetJob() {
        WaybackMachineJob newJob = new WaybackMachineJob();
        pendingJob.setJob(newJob);
        assertEquals(newJob, pendingJob.getJob(), "getJob should return the value set by setJob");
    }

    /**
     * Tests if the getFile method returns the correct FileModel object.
     */
    @Test
    void testGetFile() {
        assertEquals(file, pendingJob.getFile(), "getFile should return the expected value");
    }
}
