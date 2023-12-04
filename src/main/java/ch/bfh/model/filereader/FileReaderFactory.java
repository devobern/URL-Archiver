package ch.bfh.model.filereader;

public class FileReaderFactory {
    /**
     * Creates a FileReader based on the provided MIME type.
     *
     * @param mimeType MIME type of the file to read
     * @return FileReaderInterface implementation for the given MIME type
     * @throws IllegalArgumentException if the MIME type is unsupported
     */
    public static FileReaderInterface getFileReader(String mimeType){
        if (mimeType == null || mimeType.isEmpty()) {
            throw new IllegalArgumentException("MIME type cannot be null or empty");
        }

        if (mimeType.startsWith("text/")) {
            return new TextFileReader(); // This will handle all text/* MIME types
        } else if (mimeType.equals("application/pdf")) {
            return new PDFFileReader(); // Specific case for PDF files
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + mimeType);
        }
    }
}
