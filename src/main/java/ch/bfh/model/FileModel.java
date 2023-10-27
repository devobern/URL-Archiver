package ch.bfh.model;

import ch.bfh.helper.I18n;
import ch.bfh.exceptions.PathValidationException;
import ch.bfh.helper.FileValidator;
import ch.bfh.exceptions.FileModelException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import ch.bfh.view.ConsoleView;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Handler for validating and converting a file to a string for processing
 */
public class FileModel {

    private String mimeType;
    private String fileName;
    private String stringPath;
    private ConsoleView view;

    /**
     * Constructor for UnicodeFileHandler each file has its own handler
     * @param inputPath needs as input a path to a file
     * @throws PathValidationException throws Exception if path is invalid or path isn't a file
     */
    public FileModel(String inputPath, ConsoleView view) throws FileModelException {

        this.view = view;
        FileValidator fileValidator = new FileValidator();

        try {
            this.mimeType = fileValidator.validate(inputPath);
        } catch (IOException e) {
            throw new FileModelException(I18n.getString("file.notReadable.error"));
        } catch (PathValidationException e) {
            throw new FileModelException(I18n.getString("file.pathInvalid.error"));
        }
        this.stringPath = inputPath;
        Path path = Paths.get(inputPath.trim());
        this.fileName = path.getFileName().toString();
        view.printFormattedMessage("file.validate.info", this.fileName);


    }

    /**
     * method which converts a unicode file or pdf file to a string (not for files larger than 2GB)
     * @return
     * @throws IOException
     */
    public String fileToString() throws IOException {
        this.view.printFormattedMessage("file.toString.info", this.fileName);
        if (this.mimeType.equals("application/pdf")) {
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
