package ch.bfh.model.archiving;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link WaybackMachineJob} class.
 */
class WaybackMachineJobTest {

    private WaybackMachineJob waybackMachineJob;

    @BeforeEach
    void setUp() {
        waybackMachineJob = new WaybackMachineJob();
    }

    /**
     * Test the getter and setter for job_id.
     */
    @Test
    void testGetSetJobId() {
        String job_id = "ac58789b-f3ca-48d0-9ea6-1d1225e98695";
        waybackMachineJob.setJob_id(job_id);
        assertEquals(job_id, waybackMachineJob.getJob_id(), "Job ID should match the set value");
    }

    /**
     * Test the getter and setter for original_url.
     */
    @Test
    void testGetSetOriginalUrl() {
        String url = "http://brewster.kahle.org/";
        waybackMachineJob.setOriginal_url(url);
        assertEquals(url, waybackMachineJob.getOriginal_url(), "Original URL should match the set value");
    }

    /**
     * Test the getter and setter for status.
     */
    @Test
    void testGetSetStatus() {
        String status = "success";
        waybackMachineJob.setStatus(status);
        assertEquals(status, waybackMachineJob.getStatus(), "Status should match the set value");
    }

    /**
     * Test the getter and setter for timestamp.
     */
    @Test
    void testGetSetTimestamp() {
        String timestamp = "20180326070330";
        waybackMachineJob.setTimestamp(timestamp);
        assertEquals(timestamp, waybackMachineJob.getTimestamp(), "Timestamp should match the set value");
    }

    /**
     * Test the getter and setter for exception.
     */
    @Test
    void testGetSetException() {
        String exception = "[Errno -2] Name or service not known";
        waybackMachineJob.setException(exception);
        assertEquals(exception, waybackMachineJob.getException(), "Exception should match the set value");
    }
}
