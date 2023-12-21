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

        try {
            if (isPlainTextFile(path)) {
                return "text/plain";
            }
        } catch (IOException e) {
            throw new FileModelException(I18n.getString("folder.skipFile.info") + path.getFileName().toString() + " (" + I18n.getString("file.isEmpty.error") + ")");
        }

        throw new FileModelException(I18n.getString("folder.skipFile.info") + path.getFileName().toString() + " (" + mimeType + ")");
    }

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

}
