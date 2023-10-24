package ch.bfh.validator;

import java.io.*;

import java.nio.file.Files;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.Charset;
import java.nio.charset.CharacterCodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.nio.ByteBuffer;
import java.util.Scanner;

import ch.bfh.exceptions.UnicodeFileFormatException;
import ch.bfh.exceptions.PathValidationException;
import ch.bfh.ui.ConsoleUI;

/**
 * Validator for UnicodeFileHandler
 */
public class UnicodeFileValidator {

    /**
     * method for validating if a file is unicode encrypted or is a pdf file
     * @param stringPath -> path to the file for validation
     * @return -> returns "PDF" if the file is a PDF or the used UTF-Encryption
     * @throws UnicodeFileFormatException -> throws UnicodeFileFormat Exception if file is not a pdf or unicode encrypted
     * @throws PathValidationException -> is thrown stringPath is invalid
     */
    public static String validate(String stringPath) throws UnicodeFileFormatException, PathValidationException {
        
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
            throw new UnicodeFileFormatException(ConsoleUI.messages.getString("file.isNotFile.error"));
        }
        // check if the file is a PDF
        try {
            if (isPDF(stringPath)) {
                return "PDF";
            }
        } catch (FileNotFoundException e) {
            throw new UnicodeFileFormatException(ConsoleUI.messages.getString("file.notFound.error"));
        }

        // check if the file is unicode encrypted
        if (!isValidEncoded(stringPath, "UTF-8")) {
            throw new UnicodeFileFormatException(ConsoleUI.messages.getString("file.notUnicode.error"));
        }

        //TODO: check for other UTF-Encryptions

        return "UTF-8";

    }

    /**
     * method which checks if the first bytes are encrypted with the given charSet
     * @param path -> path to file for validation
     * @param charSet -> charset to check against
     * @return -> returns true or false
     * @throws UnicodeFileFormatException -> is thrown if the file is not found or an io exception is thrown
     */
    private static boolean isValidEncoded(String path, String charSet) throws UnicodeFileFormatException {
        byte[] buffer = new byte[4];

        // read first 4 bytes from the file
        try {
            InputStream is = new FileInputStream(path);
            is.read(buffer, 0, 4);
            is.close();
        } catch (FileNotFoundException e) {
            throw new UnicodeFileFormatException(ConsoleUI.messages.getString("file.notFound.error"));
        } catch (IOException e) {
            throw new UnicodeFileFormatException(ConsoleUI.messages.getString("file.notReadable.error"));
        }

        // try decoding the first 4 bytes with the given char set
        try {
            // if decoding with the given charset is possible return true
            Charset.availableCharsets().get(charSet).newDecoder().decode(ByteBuffer.wrap(buffer));
        } catch (CharacterCodingException e) {
            // if decoding with the given charset is not possible return false
            return false;
        }
     
        return true;
    }


    //TODO: check only first lines not the complete file

    /**
     * method for checking if file is a pdf file
     * @param path -> path to file for validation
     * @return -> return true or false
     * @throws FileNotFoundException -> is thrown if the file is not found
     */
    private static boolean isPDF(String path) throws FileNotFoundException {
        // checks if the file contains the String "%PDF-" at the beginning
        File file = new File(path);
        Scanner input = new Scanner(new FileReader(file));
        while (input.hasNextLine()) {
            final String checkLine = input.nextLine();
            if(checkLine.contains("%PDF-")) {
                // a match! return true
                return true;
            }
        }
        // the string hasn't been found -> return false
        return false;
    }

}
