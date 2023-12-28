package ch.bfh.model.archiving;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WaybackMachineArchiveResponseTest {

    private WaybackMachineArchiveResponse response;

    @BeforeEach
    void setUp() {
        response = new WaybackMachineArchiveResponse();
    }

    /**
     * Tests the getter and setter for the 'url' field.
     */
    @Test
    void testGetSetUrl() {
        String testUrl = "http://brewster.kahle.org/";
        response.setUrl(testUrl);
        assertEquals(testUrl, response.getUrl(), "Getter for url should return the value set by the setter");
    }

    /**
     * Tests the getter and setter for the 'job_id' field.
     */
    @Test
    void testGetSetJobId() {
        String job_id = "ac58789b-f3ca-48d0-9ea6-1d1225e98695";
        response.setJob_id(job_id);
        assertEquals(job_id, response.getJob_id(), "Getter for job_id should return the value set by the setter");
    }

    /**
     * Tests the getter and setter for the 'message' field.
     */
    @Test
    void testGetSetMessage() {
        String testMessage = "pending";
        response.setMessage(testMessage);
        assertEquals(testMessage, response.getMessage(), "Getter for message should return the value set by the setter");
    }
}
