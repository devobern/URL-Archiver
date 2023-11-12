package ch.bfh.model;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.action.PDAction;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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
        String result = pdfStripper.getText(document);

        // add each hyperlink with the destination to the result
        for( PDPage page : document.getPages() ) {
            List<PDAnnotation> annotations = page.getAnnotations();

            for (PDAnnotation annot : annotations) {
                if (annot instanceof PDAnnotationLink) {

                    PDAnnotationLink link = (PDAnnotationLink) annot;
                    // get link action include link url and internal link
                    PDAction action = link.getAction();

                    // add the "hidden" uri from the link to the return value
                    if (action != null) {
                        if (action instanceof PDActionURI) {
                            if (action instanceof PDActionURI) {
                                // get uri link
                                PDActionURI uri = (PDActionURI) action;
                                result = result + "\n" + uri.getURI();
                            }
                        }
                    } 

                }
            }
        }

        return result;
    }
}
