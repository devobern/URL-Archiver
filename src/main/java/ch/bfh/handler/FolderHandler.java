package ch.bfh.handler;

import ch.bfh.exceptions.FolderHandlerException;
import ch.bfh.exceptions.PathValidationException;
import ch.bfh.exceptions.UnicodeFileFormatException;
import ch.bfh.ui.ConsoleUI;
import ch.bfh.validator.FolderValidator;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FolderHandler {
    private int index = 0;
    private String basePath;
    private ArrayList<UnicodeFileHandler> unicodeFiles = new ArrayList<>();

    public FolderHandler(String inputPath) throws PathValidationException, IOException {
        FolderValidator folderValidator = new FolderValidator();
        folderValidator.validate(inputPath);
        this.basePath = inputPath;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(inputPath))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    try {
                        this.unicodeFiles.add(new UnicodeFileHandler(this.basePath + path.getFileName().toString()));
                    } catch (UnicodeFileFormatException e) {
                        ConsoleUI.printFormattedMessage("folder.skipFile.info", path.getFileName().toString());
                    }

                }
            }
        }

    }

    public boolean wasLastFile() {
        if (this.index < this.unicodeFiles.size()) {
            return false;
        } else {
            return true;
        }
    }

    public String next() throws FolderHandlerException, IOException {
        if (wasLastFile()) {
            throw new FolderHandlerException(ConsoleUI.messages.getString("folder.outOfRange.error"));
        }
        String result = this.unicodeFiles.get(index).fileToString();
        this.index = this.index + 1;
        return result;
    }
    
}
