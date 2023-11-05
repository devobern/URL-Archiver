package ch.bfh.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


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
    private List<URLPair> urlPairs;


    /**
     * Constructor for fileModel object
     * @param inputPath needs as input a path to a file
     * @param mimeType needs as input the type of the file (only plaintext and pdf are supported)
     */
    public FileModel(String inputPath, String mimeType) {
        this.mimeType = mimeType;
        this.stringPath = inputPath;
        Path path = Paths.get(inputPath.trim());
        this.fileName = path.getFileName().toString();
        this.urlPairs = new ArrayList<>();
    }

    /**
     * method which converts a unicode file or pdf file to a string (not for files larger than 2GB)
     * @return returns the file as a string object
     * @throws IOException
     */
    public String fileToString() throws IOException {
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

    public String getFileName() {
        return this.fileName;
    }

    /**
     * Sets the list of URL pairs.
     *
     * @param urlPairs the list of URL pairs to set
     */
    public void setUrlPairs(List<URLPair> urlPairs) {
        this.urlPairs = urlPairs;
    }

    /**
     * Retrieves the list of URL pairs.
     *
     * @return the list of URL pairs
     */
    public List<URLPair> getUrlPairs() {
        return urlPairs;
    }

    /**
     * Adds an extracted URL with its line number to the list.
     *
     * @param extractedURL the extracted URL to add
     * @param lineNumber   the line number where the URL was extracted from
     */
    public void addExtractedURL(String extractedURL, int lineNumber) {
        urlPairs.add(new URLPair(extractedURL, lineNumber));
    }

    /**
     * Sets the archived URL corresponding to the provided extracted URL.
     *
     * @param extractedURL the extracted URL for which the archived URL is to be set
     * @param archivedURL  the archived URL to be associated with the extracted URL
     */
    public void setArchivedURL(String extractedURL, String archivedURL) {
        for (URLPair pair : urlPairs) {
            if (pair.getExtractedURL().equals(extractedURL)) {
                pair.setArchivedURL(archivedURL);
                break;
            }
        }
    }

}
