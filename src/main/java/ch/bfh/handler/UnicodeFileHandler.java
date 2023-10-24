package ch.bfh.handler;

import ch.bfh.ui.ConsoleUI;
import ch.bfh.validator.PathValidator;
import ch.bfh.exceptions.PathValidationException;
import ch.bfh.validator.UnicodeFileValidator;
import ch.bfh.exceptions.UnicodeFileFormatException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Handler for validating and converting a file to a string for processing
 */
public class UnicodeFileHandler {

    private String charSet;
    private String fileName;
    private boolean isPDF;
    private String stringPath;

    /**
     * Constructor for UnicodeFileHandler each file has its own handler
     * @param inputPath needs as input a path to a file
     * @throws PathValidationException throws Exception if path is invalid or path isn't a file
     */
    public UnicodeFileHandler(String inputPath) throws PathValidationException, UnicodeFileFormatException {

        UnicodeFileValidator unicodeFileValidator = new UnicodeFileValidator();
        String result = unicodeFileValidator.validate(inputPath);

        this.stringPath = inputPath;
        Path path = Paths.get(inputPath.trim());
        this.fileName = path.getFileName().toString();
        ConsoleUI.printFormattedMessage("file.validate.info", this.fileName);

        if (result.equals("PDF")) {
            this.isPDF = true;
        } else {
            this.charSet = result;
            this.isPDF = false;
        }


    }

    /**
     * method which converts a unicode file or pdf file to a string (not for files larger than 2GB)
     * @return
     * @throws IOException
     */
    public String fileToString() throws IOException {
        ConsoleUI.printFormattedMessage("file.toString.info", this.fileName);
        if (this.isPDF) {
            //Loading an existing document
            File file = new File(this.stringPath);
            PDDocument document = Loader.loadPDF(file);
            //Instantiate PDFTextStripper class
            PDFTextStripper pdfStripper = new PDFTextStripper();
            //Retrieving text from PDF document
            String text = pdfStripper.getText(document);

            return text;
        } else{
            Path path = Paths.get(this.stringPath.trim());
            String text = Files.readString(path);
            return text;
        }
    }

}
