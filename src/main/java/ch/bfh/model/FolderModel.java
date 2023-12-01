package ch.bfh.model;

import ch.bfh.exceptions.FolderModelException;
import ch.bfh.helper.I18n;

import java.util.ArrayList;

/**
 * Represents a folder containing multiple file models.
 */
public class FolderModel {
    private int index = 0;
    private final String basePath;
    private final ArrayList<FileModel> files = new ArrayList<>();


    /**
     * Constructs a FolderModel with the specified directory path.
     *
     * @param inputPath The path to the directory.
     */
    public FolderModel(String inputPath) {

        if (!inputPath.endsWith("/")) {
            inputPath = inputPath + "/";
        }
        this.basePath = inputPath;

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
        FileModel file = this.files.get(index);
        this.index = this.index + 1;
        return file;
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
    public String getBasePath(){
        return this.basePath;
    }

    /**
     * Returns the list of file models in the folder.
     *
     * @return A list of FileModel objects.
     */
    public ArrayList<FileModel> getFiles() {
        return files;
    }

    public void removeFile(int i) {
        this.files.remove(i);
    }
}
