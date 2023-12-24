package ch.bfh.model.filereader;

import ch.bfh.helper.I18n;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link TextFileReader} class.
 */
class TextFileReaderTest {

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        I18n.getResourceBundle(Locale.forLanguageTag("en-US"));
    }

    /**
     * Tests successful reading of a text file's content.
     * Verifies if the content read matches the content written.
     *
     * @throws IOException if an error occurs during file creation or reading.
     */
    @Test
    @DisplayName("Read content from a text file")
    void testReadFileSuccess() throws IOException {
        Path testFile = createTestFile("Sample content");
        TextFileReader reader = new TextFileReader();

        String content = reader.readFile(testFile);

        assertEquals("Sample content", content, "Content read should match the written content");
    }

    /**
     * Tests the response to a nonexistent file path.
     * Verifies that an IOException is thrown.
     */
    @Test
    @DisplayName("Handle non-existent file")
    void testReadFileNonExistent() {
        Path nonexistentPath = tempDir.resolve("nonexistent.txt");
        TextFileReader reader = new TextFileReader();

        assertThrows(IOException.class, () -> reader.readFile(nonexistentPath),
                "Reading a non-existent file should throw IOException");
    }

    /**
     * Utility method to create a test file with specified content.
     *
     * @param content The content to write to the test file.
     * @return The path to the created test file.
     * @throws IOException if an error occurs during file writing.
     */
    private Path createTestFile(String content) throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        Files.writeString(testFile, content);
        return testFile;
    }
}
