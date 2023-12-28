package ch.bfh.model.filereader;

/**
 * Factory class for creating instances of {@link FileReaderInterface} based on MIME types.
 */
public class FileReaderFactory {

    private static final String MIME_TYPE_TEXT = "text/";
    private static final String MIME_TYPE_PDF = "application/pdf";

    /**
     * Creates a {@link FileReaderInterface} based on the provided MIME type.
     *
     * @param mimeType the MIME type of the file to be read
     * @return an implementation of {@link FileReaderInterface} suitable for the given MIME type
     * @throws IllegalArgumentException if the MIME type is null, empty, or unsupported
     */
    public static FileReaderInterface getFileReader(String mimeType) {
        validateMimeType(mimeType);

        if (mimeType.startsWith(MIME_TYPE_TEXT)) {
            return new TextFileReader(); // Handles all text/* MIME types
        } else if (mimeType.equals(MIME_TYPE_PDF)) {
            return new PDFFileReader(); // Specific handler for PDF files
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + mimeType);
        }
    }

    /**
     * Validates the provided MIME type.
     *
     * @param mimeType the MIME type to validate
     * @throws IllegalArgumentException if the MIME type is null or empty
     */
    private static void validateMimeType(String mimeType) {
        if (mimeType == null || mimeType.isEmpty()) {
            throw new IllegalArgumentException("MIME type cannot be null or empty");
        }
    }
}
