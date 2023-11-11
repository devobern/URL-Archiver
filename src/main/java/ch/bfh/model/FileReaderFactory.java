package ch.bfh.model;

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

        return switch (mimeType) {
            case "application/pdf" -> new PDFFileReader();
            case "text/plain" -> new TextFileReader();
            default -> throw new IllegalArgumentException("Unsupported file type" + mimeType);
        };


    }
}
