package ch.bfh.model.filereader;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Defines the contract for file reader implementations.
 * Implementations are expected to read content from a file and perform operations based on the file type.
 */
public interface FileReaderInterface {
    /**
     * Reads the content from a file and returns it as a String.
     *
     * @param filePath the path to the file to be read.
     * @return the content of the file as a String.
     * @throws IOException if an error occurs during reading.
     */
    String readFile(Path filePath) throws IOException;
}
