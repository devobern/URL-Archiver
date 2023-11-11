package ch.bfh.model;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * A concrete implementation of FileReaderInterface for reading PDF files.
 */
public class PDFFileReader implements FileReaderInterface{
    /**
     * Reads the content from a PDF file located at the specified path.
     *
     * @param filePath the path of the PDF file to read.
     * @return the content of the file as a String.
     * @throws IOException if the PDF cannot be read.
     */
    @Override
    public String readFile(Path filePath) throws IOException {
        //Loading an existing document
        File file = new File(String.valueOf(filePath));
        PDDocument document = Loader.loadPDF(file);
        //Instantiate PDFTextStripper class
        PDFTextStripper pdfStripper = new PDFTextStripper();
        //Retrieving text from PDF document
        return pdfStripper.getText(document);
    }
}
