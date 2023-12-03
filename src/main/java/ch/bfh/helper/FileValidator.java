package ch.bfh.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ch.bfh.exceptions.FileModelException;

/**
 * Validator for file paths that determines the type of the file and if it's supported.
 */
public class FileValidator {

    /**
     * Validates if a file is a PDF or a Unicode encoded text file.
     *
     * @param stringPath Path to the file for validation.
     * @return Returns "application/pdf" if the file is a PDF or "text/*" if it's a text file.
     * @throws FileModelException if the file is not a PDF or Unicode encoded text file.
     * @throws IOException if an I/O error occurs.
     */
    public static String validate(String stringPath) throws FileModelException, IOException {
        Path path = Paths.get(stringPath.trim());

        if (!Files.isRegularFile(path)) {
            throw new FileModelException(I18n.getString("file.isNotFile.error"));
        }

        String mimeType = Files.probeContentType(path);

        if (mimeType == null) {
            mimeType = inferMimeTypeFromExtension(path);
        }

        if (mimeType != null && (mimeType.contains("text/") || mimeType.equals("application/pdf"))) {
            return mimeType;
        } else {
            throw new FileModelException(I18n.getString("folder.skipFile.info") + path.getFileName().toString());
        }
    }

    private static String inferMimeTypeFromExtension(Path path) {
        String fileName = path.getFileName().toString();
        int index = fileName.lastIndexOf(".");
        if (index > 0 && fileName.substring(index + 1).equalsIgnoreCase("bib")) {
            return "text/bib";
        }
        return null;
    }
}
