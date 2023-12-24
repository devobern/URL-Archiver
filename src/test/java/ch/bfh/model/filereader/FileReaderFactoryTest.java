package ch.bfh.model.filereader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link FileReaderFactory} class.
 */
class FileReaderFactoryTest {

    /**
     * Test to ensure that a TextFileReader is created for text MIME types.
     */
    @Test
    @DisplayName("Create TextFileReader for text MIME type")
    void testGetTextFileReader() {
        String mimeType = "text/plain";
        FileReaderInterface reader = FileReaderFactory.getFileReader(mimeType);
        assertInstanceOf(TextFileReader.class, reader, "Should return TextFileReader for text MIME type");
    }

    /**
     * Test to ensure that a PDFFileReader is created for the PDF MIME type.
     */
    @Test
    @DisplayName("Create PDFFileReader for PDF MIME type")
    void testGetPdfFileReader() {
        String mimeType = "application/pdf";
        FileReaderInterface reader = FileReaderFactory.getFileReader(mimeType);
        assertInstanceOf(PDFFileReader.class, reader, "Should return PDFFileReader for PDF MIME type");
    }

    /**
     * Test to verify that an IllegalArgumentException is thrown for unsupported MIME types.
     */
    @Test
    @DisplayName("Throw exception for unsupported MIME type")
    void testGetFileReaderUnsupportedMimeType() {
        String mimeType = "image/jpeg";
        assertThrows(IllegalArgumentException.class, () -> FileReaderFactory.getFileReader(mimeType),
                "Should throw IllegalArgumentException for unsupported MIME type");
    }

    /**
     * Test to confirm that passing a null MIME type to the factory throws an IllegalArgumentException.
     */
    @Test
    @DisplayName("Throw exception for null MIME type")
    void testGetFileReaderNullMimeType() {
        assertThrows(IllegalArgumentException.class, () -> FileReaderFactory.getFileReader(null),
                "Should throw IllegalArgumentException for null MIME type");
    }

    /**
     * Test to ensure that passing an empty string as MIME type results in an IllegalArgumentException.
     */
    @Test
    @DisplayName("Throw exception for empty MIME type")
    void testGetFileReaderEmptyMimeType() {
        String mimeType = "";
        assertThrows(IllegalArgumentException.class, () -> FileReaderFactory.getFileReader(mimeType),
                "Should throw IllegalArgumentException for empty MIME type");
    }
}
