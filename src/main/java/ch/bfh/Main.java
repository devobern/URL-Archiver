package ch.bfh;

import ch.bfh.exceptions.FolderHandlerException;
import ch.bfh.exceptions.PathValidationException;
import ch.bfh.exceptions.UnicodeFileHandlerException;
import ch.bfh.handler.FolderHandler;
import ch.bfh.handler.UnicodeFileHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws UnicodeFileHandlerException, PathValidationException, IOException, FolderHandlerException {

        java.util.logging.Logger.getLogger("org.apache.pdfbox")
                .setLevel(java.util.logging.Level.OFF);

        System.out.println("Hello world!");

        UnicodeFileHandler unicodeFileHandler = new UnicodeFileHandler("/Users/kilian/Downloads/test.txt");
        System.out.println(unicodeFileHandler.fileToString());

        unicodeFileHandler = new UnicodeFileHandler("/Users/kilian/Downloads/UI_TeamC.pdf");
        System.out.println(unicodeFileHandler.fileToString());

        FolderHandler folderHandler = new FolderHandler("/Users/kilian/Downloads/");
        while (!folderHandler.wasLastFile()) {
            System.out.println(folderHandler.next());
        }
    }
}