package ch.bfh.model.filereader;

import ch.bfh.helper.I18n;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implementation of FileReaderInterface for reading plain text files.
 */
public class TextFileReader implements FileReaderInterface {

    /**
     * Reads and returns the content of a text file at the given path.
     *
     * @param filePath Path of the text file to be read.
     * @return String containing the file's content.
     * @throws IOException if there is an error reading the file.
     */
    @Override
    public String readFile(Path filePath) throws IOException {
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new IOException(I18n.getString("error.reading_file") + " " + filePath, e);
        }
    }
}
