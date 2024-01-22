package ch.bfh.helper;

import ch.bfh.exceptions.FileModelException;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link FileValidator} helper class.
 */
class FileValidatorTest {

    @TempDir
    Path tempDir;

    private Path pdfPath;
    private Path textPath;
    private Path bibPath;
    private Path jpgPath;

    /**
     * Helper method to create a file with specified content in the test directory.
     *
     * @param fileName Name of the file to create.
     * @param content  Content to write into the file.
     * @return The path to the created file.
     * @throws IOException if writing to the file fails.
     */
    private Path createFileWithContent(String fileName, String content) throws IOException {
        Path path = tempDir.resolve(fileName);
        Files.writeString(path, content);
        return path;
    }

    /**
     * Mocks file-related methods for the specified path and MIME type.
     *
     * @param path     The file path.
     * @param mimeType The MIME type to return for the file.
     */
    private void mockFileMethods(Path path, String mimeType) {
        try (MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.probeContentType(path)).thenReturn(mimeType);
            filesMockedStatic.when(() -> Files.isRegularFile(path)).thenReturn(true);
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        I18n.getResourceBundle(Locale.forLanguageTag("en-US"));

        pdfPath = createFileWithContent("test.pdf", "%PDF-1.4");
        textPath = createFileWithContent("test.txt", "Sample text content");
        bibPath = createFileWithContent("test.bib", "@article{...}");
        jpgPath = createFileWithContent("test.jpg", Arrays.toString(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}));
    }

    /**
     * Validates that a PDF file is correctly identified.
     */
    @Test
    void validatePdfFile() throws IOException, FileModelException {
        mockFileMethods(pdfPath, "application/pdf");
        assertEquals("application/pdf", FileValidator.validate(pdfPath.toString()));
    }

    /**
     * Validates that a text file is correctly identified.
     */
    @Test
    void validateTextFile() throws IOException, FileModelException {
        mockFileMethods(textPath, "text/plain");
        assertEquals("text/plain", FileValidator.validate(textPath.toString()));
    }

    /**
     * Validates a .bib file on Windows systems.
     */
    @Test
    void validateBibFileWindows() throws IOException, FileModelException {
        Assumptions.assumeTrue(System.getProperty("os.name").toLowerCase().contains("win"));
        assertEquals("text/bib", FileValidator.validate(bibPath.toString()));
    }

    /**
     * Validates a .bib file on Linux systems.
     */
    @Test
    void validateBibFileLinux() throws IOException, FileModelException {
        Assumptions.assumeTrue(System.getProperty("os.name").toLowerCase().contains("nux"));
        mockFileMethods(bibPath, "text/x-bibtex");
        // Apparently, on Fedora the MIME type for BibTeX files is "text/bib"
        List<String> validValues = Arrays.asList("text/x-bibtex", "text/bib");
        String result = FileValidator.validate(bibPath.toString());
        assertTrue(validValues.contains(result));
    }

    /**
     * Validates a .bib file on Mac systems.
     */
    @Test
    void validateBibFileMac() throws IOException, FileModelException {
        Assumptions.assumeTrue(System.getProperty("os.name").toLowerCase().contains("mac"));
        mockFileMethods(bibPath, "text/x-bibtex");
        assertEquals("text/bib", FileValidator.validate(bibPath.toString()));
    }

    /**
     * Validates that unsupported file types are correctly rejected.
     */
    @Test
    void validateUnsupportedFileType() {
        try (MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.probeContentType(jpgPath))
                    .thenReturn("image/jpeg");
            filesMockedStatic.when(() -> Files.isRegularFile(jpgPath))
                    .thenReturn(true);
            filesMockedStatic.when(() -> Files.newInputStream(jpgPath))
                    .thenReturn(new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}));

            FileModelException exception = assertThrows(
                    FileModelException.class,
                    () -> FileValidator.validate(jpgPath.toString())
            );
            assertTrue(exception.getMessage().contains(jpgPath.getFileName().toString()));
        }
    }

    /**
     * Validates the behavior for non-existent files.
     */
    @Test
    void validateNonExistentFile() {
        Path invalidPath = tempDir.resolve("nonexistent.file");
        assertThrows(FileModelException.class, () -> FileValidator.validate(invalidPath.toString()));
    }

    /**
     * Validates that directories are not accepted as valid file paths.
     */
    @Test
    void validateDirectoryPath() {
        assertThrows(FileModelException.class, () -> FileValidator.validate(tempDir.toString()));
    }

    /**
     * Validates the handling of malformed file paths.
     */
    @Test
    void validateMalformedPath() {
        assertThrows(FileModelException.class, () -> FileValidator.validate("\0invalidPath.txt"));
    }

    /**
     * Validates the handling of empty string paths.
     */
    @Test
    void validateEmptyStringPath() {
        assertThrows(FileModelException.class, () -> FileValidator.validate(""));
    }
}
