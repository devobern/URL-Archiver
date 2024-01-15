package ch.bfh.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
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
     * @throws IOException        if an I/O error occurs.
     */
    public static String validate(String stringPath) throws FileModelException, IOException {
        Path path = Paths.get(stringPath.trim());

        if (!Files.isRegularFile(path)) {
            throw new FileModelException(I18n.getString("file.isNotFile.error"));
        }

        String mimeType = Files.probeContentType(path);
        if (mimeType != null) {
            if (mimeType.equals("application/pdf") || mimeType.startsWith("text/")) {
                return mimeType;
            }
        }

        // Check if the file is a .bib file. This step is necessary only on Windows, as MimeTypes are handled differently on windows.
        if (getFileExtension(stringPath).equals("bib")) {
            return "text/bib";
        }

        try {
            if (isPlainTextFile(path)) {
                return "text/plain";
            }
        } catch (IOException e) {
            throw new FileModelException(I18n.getString("folder.skipFile.info") + path.getFileName().toString() + " (" + I18n.getString("file.isEmpty.error") + ")");
        }

        throw new FileModelException(I18n.getString("folder.skipFile.info") + path.getFileName().toString() + " (" + mimeType + ")");
    }

    /**
     * Checks if the given file path refers to a plain text file.
     *
     * @param path The path of the file to be checked.
     * @return {@code true} if the file is a plain text file, {@code false} otherwise.
     * @throws IOException If an I/O error occurs, such as if the file is empty or cannot be read.
     */
    private static boolean isPlainTextFile(Path path) throws IOException {
        try (InputStream in = Files.newInputStream(path)) {
            byte[] buffer = new byte[1024];
            int length = in.read(buffer);
            if (length == -1) {
                // Empty file or end of file reached
                throw new IOException("File is empty");
            }
            return isText(buffer, length);
        }
    }

    /**
     * Determines if the provided byte array represents text.
     * This method attempts to decode the array using UTF-8 encoding. If decoding is successful and the result is non-empty, the data is considered to be text.
     *
     * @param data   The byte array to check.
     * @param length The number of bytes to use from the array.
     * @return {@code true} if the specified portion of the array represents text, {@code false} otherwise.
     */
    private static boolean isText(byte[] data, int length) {
        if (length <= 0) {
            return false;
        }

        int actualLength = Math.min(length, data.length);
        try {
            ByteBuffer buffer = ByteBuffer.wrap(data, 0, actualLength);
            return !StandardCharsets.UTF_8.newDecoder().decode(buffer).toString().isEmpty();
        } catch (CharacterCodingException e) {
            return false;
        }
    }

    /**
     * Extracts the file extension from a file path.
     * This method considers the part of the string after the last period ('.') as the file extension. If there is no period or the last period is part of a directory name, an empty string is returned.
     *
     * @param filePath The file path from which to extract the extension.
     * @return The file extension, or an empty string if no extension is found.
     */
    private static String getFileExtension(String filePath) {
        // Find the last index of a period
        int lastIndexOfPeriod = filePath.lastIndexOf(".");

        // Find the last index of a file separator
        int lastIndexOfSeparator = Math.max(filePath.lastIndexOf("/"), filePath.lastIndexOf("\\"));

        // Check if the last period is part of the filename (not a directory)
        if (lastIndexOfPeriod > lastIndexOfSeparator) {
            return filePath.substring(lastIndexOfPeriod + 1);
        } else {
            return ""; // No extension found
        }
    }
}
