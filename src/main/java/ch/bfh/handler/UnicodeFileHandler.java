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

public class UnicodeFileHandler {

    private String charSet;
    private String fileName;
    private Boolean isPDF;
    private String stringPath;

    /**
     * Constructor for UnicodeFileHandler each file has its own handler
     * @param inputPath needs as input a path to a file
     * @throws PathValidationException throws Exception if path is invalid or path isn't a file
     */
    public UnicodeFileHandler(String inputPath) throws PathValidationException, UnicodeFileFormatException {
        this.stringPath = inputPath;
        Path path = Paths.get(inputPath.trim());
        this.fileName = path.getFileName().toString();
        System.out.println("Validate the file " + this.fileName);

        PathValidator pathValidator = new PathValidator();
        pathValidator.validate(inputPath);

        UnicodeFileValidator unicodeFileValidator = new UnicodeFileValidator();
        this.charSet = unicodeFileValidator.validate(inputPath);



        try {
            if (isPDF()) {
                this.isPDF = Boolean.TRUE;
            } else {
                this.isPDF = Boolean.FALSE;
            }
        } catch (FileNotFoundException e) {
            throw new UnicodeFileFormatException(ConsoleUI.messages.getString("file.notFound.error"));
        }

    }

    public String fileToString() throws IOException {
        System.out.println("Convert the file " + this.fileName + " to String for processing.");
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

    private boolean isPDF() throws FileNotFoundException {
        File file = new File(this.stringPath);
        Scanner input = new Scanner(new FileReader(file));
        while (input.hasNextLine()) {
            final String checkline = input.nextLine();
            if(checkline.contains("%PDF-")) {
                // a match!
                return true;
            }
        }
        return false;
    }

}
