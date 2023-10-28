package ch.bfh.helper;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;

import ch.bfh.exceptions.FileModelException;
import ch.bfh.exceptions.PathValidationException;

/**
 * Validator for UnicodeFileHandler
 */
public class FileValidator {


    /**
     * method for validating if a file is unicode encrypted or is a pdf file
     *
     * @param stringPath -> path to the file for validation
     * @return -> returns "PDF" if the file is a PDF or the used UTF-Encryption
     * @throws FileModelException -> throws UnicodeFileFormat Exception if file is not a pdf or unicode encrypted
     * @throws PathValidationException     -> is thrown stringPath is invalid
     */
    public static String validate(String stringPath) throws FileModelException, PathValidationException, IOException {

        Path path;
        try {
            // Use trim() to remove accidentally added leading and trailing
            // whitespaces for better user experience.
            path = Paths.get(stringPath.trim());
        } catch (InvalidPathException e) {
            throw new PathValidationException(I18n.getString("path.invalid.error"));
        }

        // check if the path is a file
        if (!Files.isRegularFile(path)) {
            throw new FileModelException(I18n.getString("file.isNotFile.error"));
        }


        String mimeType = Files.probeContentType(path);

        if (mimeType != null && (mimeType.equals("text/plain") || mimeType.equals("application/pdf"))) {
            return mimeType;
        } else {
            throw new FileModelException(I18n.getString("file.notSupported.error"));
        }

    }

}