package ch.bfh;

import ch.bfh.exceptions.PathValidationException;
import ch.bfh.exceptions.UnicodeFileFormatException;
import ch.bfh.handler.UnicodeFileHandler;

public class Main {
    public static void main(String[] args) throws UnicodeFileFormatException, PathValidationException {

        System.out.println("Hello world!");

        UnicodeFileHandler unicodeFileHandler = new UnicodeFileHandler("/Users/kilian/Downloads/Empathie im Berufsleben_Kilian.pptx");


    }
}