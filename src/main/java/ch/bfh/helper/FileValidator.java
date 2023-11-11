package ch.bfh.helper;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;

import ch.bfh.exceptions.FileModelException;
import ch.bfh.exceptions.PathValidationException;

/**
 * Validator for file path
 * determines the type of the file and if its supported
 */
public class FileValidator {


    /**
     * method for validating if a file is unicode encrypted or is a pdf file
     *
     * @param stringPath -> path to the file for validation
     * @return -> returns "PDF" if the file is a PDF or the used UTF-Encryption
     * @throws FileModelException -> throws UnicodeFileFormat Exception if file is not a pdf or unicode encrypted
     */
    public static String validate(String stringPath) throws FileModelException, IOException {

        Path path = Paths.get(stringPath.trim());

        // check if the path is a file
        if (!Files.isRegularFile(path)) {
            throw new FileModelException(I18n.getString("file.isNotFile.error"));
        }


        String mimeType = Files.probeContentType(path);

        // probeContentType doesn't discover .bib files so it's checked with the file extension
        if (mimeType == null) {
            String fileName = path.getFileName().toString();
            int index = fileName.lastIndexOf(".");
            if (fileName.substring(index + 1).equals("bib")) {
                mimeType = "text/bib";
            }
        }


        if (mimeType != null && (mimeType.contains("text/") || mimeType.equals("application/pdf"))) {
            return mimeType;
        } else {
            throw new FileModelException(I18n.getString("file.notSupported.error"));
        }

    }

}
