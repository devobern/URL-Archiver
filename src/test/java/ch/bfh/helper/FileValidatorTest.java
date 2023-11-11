package ch.bfh.helper;

import ch.bfh.exceptions.FileModelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link FileValidator} helper class.
 */
class FileValidatorTest {

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        // Initialize I18n resource bundle for internationalization support.
        I18n.getResourceBundle(Locale.forLanguageTag("en-US"));
    }

    private void createFileWithContent(Path dir, String fileName, String content) throws IOException {
        // Helper method to create a file with specified content in the test directory.
        Files.writeString(dir.resolve(fileName), content);
    }

    @Test
    void validatePdfFile() throws IOException, FileModelException {
        // Test to ensure that PDF files are correctly identified by the FileValidator.
        String fileName = "test.pdf";
        createFileWithContent(tempDir, fileName, "%PDF-1.4"); // Mock PDF content
        assertEquals("application/pdf", FileValidator.validate(tempDir.resolve(fileName).toString()));
    }

    @Test
    void validateTextFile() throws IOException, FileModelException {
        // Test to ensure that text files are correctly identified by the FileValidator.
        String fileName = "test.txt";
        createFileWithContent(tempDir, fileName, "Sample text content");
        assertEquals("text/plain", FileValidator.validate(tempDir.resolve(fileName).toString()));
    }

    @Test
    void validateBibFile() throws IOException, FileModelException {
        // Test to ensure that .bib files are correctly identified as 'text/bib' by the FileValidator.
        String fileName = "test.bib";
        createFileWithContent(tempDir, fileName, "@article{...}");
        assertEquals("text/bib", FileValidator.validate(tempDir.resolve(fileName).toString()));
    }

    @Test
    void validateUnsupportedFileType() throws IOException {
        // Test to check that the FileValidator throws an exception for unsupported file types.
        String fileName = "image.jpg";
        createFileWithContent(tempDir, fileName, "Mock image content");
        FileModelException exception = assertThrows(FileModelException.class,
                () -> FileValidator.validate(tempDir.resolve(fileName).toString()));
        assertEquals(I18n.getString("file.notSupported.error"), exception.getMessage());
    }

    @Test
    void validateNonExistentFile() {
        // Test to verify that the FileValidator throws an exception for non-existent files.
        Path invalidPath = tempDir.resolve("nonexistent.file");
        assertThrows(FileModelException.class, () -> FileValidator.validate(invalidPath.toString()));
    }

    @Test
    void validateDirectoryPath() {
        // Test to check that the FileValidator throws an exception when the provided path is a directory.
        FileModelException exception = assertThrows(FileModelException.class,
                () -> FileValidator.validate(tempDir.toString()));
        assertEquals(I18n.getString("file.isNotFile.error"), exception.getMessage());
    }

    @Test
    void validateMalformedPath() {
        // Test to ensure that the FileValidator throws an exception for malformed file paths.
        String malformedPath = "\0invalidPath.txt";
        assertThrows(FileModelException.class, () -> FileValidator.validate(malformedPath));
    }

    @Test
    void validateEmptyStringPath() {
        // Test to verify that the FileValidator throws an exception for empty string paths.
        assertThrows(FileModelException.class, () -> FileValidator.validate(""));
    }
}
