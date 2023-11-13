package ch.bfh.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A concrete implementation of FileReaderInterface for reading plain text files.
 */
public class TextFileReader implements FileReaderInterface{
    /**
     * Reads the content from a text file located at the specified path.
     *
     * @param filePath the path of the file to read.
     * @return the content of the file as a String.
     * @throws IOException if the file cannot be read.
     */
    @Override
    public String readFile(Path filePath) throws IOException {
        return Files.readString(filePath);
    }
}
