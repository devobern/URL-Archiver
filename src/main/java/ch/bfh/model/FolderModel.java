package ch.bfh.model;

import ch.bfh.exceptions.FolderModelException;
import ch.bfh.exceptions.PathValidationException;
import ch.bfh.exceptions.FileModelException;
import ch.bfh.helper.I18n;
import ch.bfh.helper.FolderValidator;
import ch.bfh.view.ConsoleView;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FolderModel {
    private int index = 0;
    private String basePath;
    private ArrayList<FileModel> files = new ArrayList<>();
    private ConsoleView view;

    public FolderModel(String inputPath, ConsoleView view) throws PathValidationException, IOException {
        FolderValidator folderValidator = new FolderValidator();
        folderValidator.validate(inputPath);
        if (!inputPath.endsWith("/")) {
            inputPath = inputPath + "/";
        }
        this.basePath = inputPath;
        this.view = view;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(inputPath))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    try {
                        this.files.add(new FileModel(this.basePath + path.getFileName().toString(), this.view));
                    } catch (FileModelException e) {
                        view.printFormattedMessage("folder.skipFile.info", path.getFileName().toString());
                    }

                }
            }
        }

    }

    public boolean wasLastFile() {
        if (this.index < this.files.size()) {
            return false;
        } else {
            return true;
        }
    }

    public String next() throws FolderModelException, IOException {
        if (wasLastFile()) {
            throw new FolderModelException(I18n.getString("folder.outOfRange.error"));
        }
        String result = this.files.get(index).fileToString();
        this.index = this.index + 1;
        return result;
    }
    
}
