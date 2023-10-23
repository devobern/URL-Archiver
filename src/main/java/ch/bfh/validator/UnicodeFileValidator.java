package ch.bfh.validator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.Charset;
import java.nio.charset.CharacterCodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.nio.ByteBuffer;

import ch.bfh.exceptions.UnicodeFileFormatException;
import ch.bfh.exceptions.PathValidationException;
import ch.bfh.ui.ConsoleUI;

public class UnicodeFileValidator {
    
    public static void validate(String stringPath) throws UnicodeFileFormatException, PathValidationException {
        
        Path path;
        try {
            // Use trim() to remove accidentally added leading and trailing
            // whitespaces for better user experience.
            path = Paths.get(stringPath.trim());
        } catch (InvalidPathException e) {
            throw new PathValidationException(ConsoleUI.messages.getString("path.invalid.error"));
        }

        if (!Files.isRegularFile(path)) {
            throw new UnicodeFileFormatException(ConsoleUI.messages.getString("file.isNotFile.error"));
        }

        if (!isValidEncoded(stringPath, "UTF-8")) {
            throw new UnicodeFileFormatException(ConsoleUI.messages.getString("file.notUnicode.error"));
        }

    }


    private static boolean isValidEncoded(String path, String charSet) throws UnicodeFileFormatException {
        byte[] buffer = new byte[4];

        try {
            InputStream is = new FileInputStream(path);
            is.read(buffer, 0, 4);
            is.close();
        } catch (FileNotFoundException e) {
            throw new UnicodeFileFormatException(ConsoleUI.messages.getString("file.notFound.error"));
        } catch (IOException e) {
            throw new UnicodeFileFormatException(ConsoleUI.messages.getString("file.notReadable.error"));
        }
        
        
        

        try {
            Charset.availableCharsets().get(charSet).newDecoder().decode(ByteBuffer.wrap(buffer));
     
        } catch (CharacterCodingException e) {
     
            return false;
        }
     
        return true;
    }

}
