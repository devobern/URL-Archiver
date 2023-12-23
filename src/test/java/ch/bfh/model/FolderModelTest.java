package ch.bfh.model;

import ch.bfh.exceptions.FolderModelException;
import ch.bfh.helper.I18n;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class FolderModelTest {

    @TempDir
    Path tempDir;

    private FolderModel folder;

    @BeforeEach
    void setUp() {
        I18n.getResourceBundle(Locale.forLanguageTag("en-US"));

        folder = new FolderModel(tempDir.toString());
        folder.addFile(new FileModel(Path.of("testTextFolderModelTest.txt"), "text/plain"));
        folder.addFile(new FileModel(Path.of("testPDFFolderModelTest.pdf"), "application/pdf"));
    }

    /**
     * Test the wasLastFile method in FolderModel.
     * Ensures it returns true after iterating over all files in the folder.
     */
    @Test
    void testWasLastFile() {
        try {
            folder.next();
            folder.next();
        } catch (FolderModelException e) {
            fail("Exception thrown while iterating files: " + e.getMessage());
        }

        assertTrue(folder.wasLastFile(), "wasLastFile should return true after iterating all files");
    }

    /**
     * Test the next method in FolderModel.
     * Validates if the method throws FolderModelException after the last file.
     */
    @Test
    void testNext_ThrowsExceptionAfterLastFile() {
        try {
            folder.next();
            folder.next();
        } catch (FolderModelException e) {
            fail("Exception thrown prematurely while iterating files: " + e.getMessage());
        }

        assertThrows(FolderModelException.class, folder::next, "FolderModelException should be thrown after the last file");
    }

    /**
     * Test the behavior when no files are added to the FolderModel.
     * Verifies that wasLastFile returns true and next throws FolderModelException.
     */
    @Test
    void testBehaviorWithNoFiles() {
        FolderModel emptyFolder = new FolderModel(".");
        assertTrue(emptyFolder.wasLastFile(), "wasLastFile should return true for an empty folder");
        assertThrows(FolderModelException.class, emptyFolder::next, "FolderModelException should be thrown for an empty folder");
    }

    /**
     * Test removing a file from the FolderModel.
     * Validates that the file count decreases after removal.
     */
    @Test
    void testRemoveFile() {
        int initialSize = folder.getFiles().size();
        folder.removeFile(0); // Remove first file
        assertEquals(initialSize - 1, folder.getFiles().size(), "File count should decrease after removal");
    }

    /**
     * Test getting the base path of the folder.
     * Validates that the correct base path is returned.
     */
    @Test
    void testGetBasePath() {
        String expectedBasePath = tempDir.toString() + File.separator;
        assertEquals(expectedBasePath, folder.getBasePath(), "Base path should match the temporary directory path");
    }
}
