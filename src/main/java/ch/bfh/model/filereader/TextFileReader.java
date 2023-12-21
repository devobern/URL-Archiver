package ch.bfh.model.filereader;

import ch.bfh.helper.I18n;

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
    public String readFile(Path filePath) throws IOException {
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new IOException(I18n.getString("error.reading_file") + " " + filePath, e);
        }
    }
}
