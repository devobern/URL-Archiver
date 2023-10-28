package ch.bfh.model;

import ch.bfh.exceptions.FolderModelException;
import ch.bfh.helper.I18n;

import java.util.ArrayList;

/**
 * a folder object contains a list of file (FileModel) objects
 */
public class FolderModel {
    private int index = 0;
    private String basePath;
    private ArrayList<FileModel> files = new ArrayList<>();


    /**
     * constructor for a folder object
     * @param inputPath needs the path to the folder as a string
     */
    public FolderModel(String inputPath) {

        if (!inputPath.endsWith("/")) {
            inputPath = inputPath + "/";
        }
        this.basePath = inputPath;

    }

    /**
     * returns if the last file was iteratet through
     * @return
     */
    public boolean wasLastFile() {
        if (this.index < this.files.size()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * returns the next file object from the list
     * @return
     * @throws FolderModelException
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
     * add a file object to the list
     * @param file
     */
    public void addFile(FileModel file) {
        this.files.add(file);
    }

    public String getBasePath(){
        return this.basePath;
    }
    
}
