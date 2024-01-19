package ch.bfh.model;

import ch.bfh.exceptions.FolderModelException;
import ch.bfh.helper.I18n;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a folder containing multiple file models.
 */
public class FolderModel {
    private final String basePath;
    private final List<FileModel> files = new ArrayList<>();
    private int index = 0;

    /**
     * Constructs a FolderModel with the specified directory path.
     * Ensures the path ends with a file separator.
     *
     * @param inputPath The path to the directory.
     */
    public FolderModel(String inputPath) {
        this.basePath = ensureEndsWithSeparator(inputPath);
    }

    /**
     * Checks whether all files have been iterated over.
     *
     * @return True if all files have been iterated, false otherwise.
     */
    public boolean wasLastFile() {
        return this.index >= this.files.size();
    }

    /**
     * Retrieves the next file in the iteration.
     *
     * @return The next FileModel object.
     * @throws FolderModelException if there are no more files to return.
     */
    public FileModel next() throws FolderModelException {
        if (wasLastFile()) {
            throw new FolderModelException(I18n.getString("folder.outOfRange.error"));
        }
        return this.files.get(index++);
    }

    /**
     * Adds a file model to the folder's list of files.
     *
     * @param file The FileModel to add.
     */
    public void addFile(FileModel file) {
        this.files.add(file);
    }

    /**
     * Returns the base path of the folder.
     *
     * @return The base directory path as a string.
     */
    public String getBasePath() {
        return this.basePath;
    }

    /**
     * Returns the list of file models in the folder.
     *
     * @return A list of FileModel objects.
     */
    public List<FileModel> getFiles() {
        return files;
    }

    /**
     * Removes the file at the specified index from the folder.
     *
     * @param index The index of the file to remove.
     */
    public void removeFile(int index) {
        this.files.remove(index);
    }

    /**
     * Ensures that the input path ends with a file separator.
     *
     * @param path The path to ensure.
     * @return The path ending with a file separator.
     */
    private String ensureEndsWithSeparator(String path) {
        return path.endsWith(File.separator) ? path : path + File.separator;
    }
}
