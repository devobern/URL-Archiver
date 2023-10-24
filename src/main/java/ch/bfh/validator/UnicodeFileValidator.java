package ch.bfh.validator;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.util.Scanner;

import ch.bfh.exceptions.UnicodeFileHandlerException;
import ch.bfh.exceptions.PathValidationException;
import ch.bfh.ui.ConsoleUI;

/**
 * Validator for UnicodeFileHandler
 */
public class UnicodeFileValidator {

    //TODO: validation doesn't work --> example .docx files

    /**
     * method for validating if a file is unicode encrypted or is a pdf file
     *
     * @param stringPath -> path to the file for validation
     * @return -> returns "PDF" if the file is a PDF or the used UTF-Encryption
     * @throws UnicodeFileHandlerException -> throws UnicodeFileFormat Exception if file is not a pdf or unicode encrypted
     * @throws PathValidationException     -> is thrown stringPath is invalid
     */
    public static String validate(String stringPath) throws UnicodeFileHandlerException, PathValidationException, IOException {

        Path path;
        try {
            // Use trim() to remove accidentally added leading and trailing
            // whitespaces for better user experience.
            path = Paths.get(stringPath.trim());
        } catch (InvalidPathException e) {
            throw new PathValidationException(ConsoleUI.messages.getString("path.invalid.error"));
        }

        // check if the path is a file
        if (!Files.isRegularFile(path)) {
            throw new UnicodeFileHandlerException(ConsoleUI.messages.getString("file.isNotFile.error"));
        }


        String mimeType = Files.probeContentType(path);

        if (mimeType != null && (mimeType.equals("text/plain") || mimeType.equals("application/pdf"))) {
            return mimeType;
        } else {
            throw new UnicodeFileHandlerException(ConsoleUI.messages.getString("file.notSupported.error"));
        }

    }

}
