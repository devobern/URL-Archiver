package ch.bfh;

import ch.bfh.exceptions.PathValidationException;
import ch.bfh.exceptions.UnicodeFileFormatException;
import ch.bfh.handler.UnicodeFileHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws UnicodeFileFormatException, PathValidationException, IOException {

        System.out.println("Hello world!");

        UnicodeFileHandler unicodeFileHandler = new UnicodeFileHandler("/Users/kilian/Downloads/test.txt");
        System.out.println(unicodeFileHandler.fileToString());

        unicodeFileHandler = new UnicodeFileHandler("/Users/kilian/Downloads/UI_TeamC.pdf");
        System.out.println(unicodeFileHandler.fileToString());

    }
}