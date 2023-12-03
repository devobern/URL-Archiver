package ch.bfh.model.filereader;

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
 * Implementation of FileReaderInterface for reading PDF files.
 * This class provides functionality to extract text and hyperlinks from PDF documents.
 */
public class PDFFileReader implements FileReaderInterface {

    /**
     * Reads the content from a PDF file located at the specified path.
     * It extracts both the text and any embedded hyperlinks in the document.
     *
     * @param filePath the path of the PDF file to read.
     * @return a string containing the text content and hyperlinks of the file.
     * @throws IOException if an error occurs during reading the PDF.
     */
    @Override
    public String readFile(Path filePath) throws IOException {
        StringBuilder result = new StringBuilder();

        try (PDDocument document = Loader.loadPDF(new File(filePath.toString()))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            result.append(pdfStripper.getText(document));
            extractHyperlinks(document, result);
        }

        return result.toString();
    }

    /**
     * Extracts hyperlinks from the given PDF document and appends them to the result.
     *
     * @param document the PDF document to extract hyperlinks from.
     * @param result   the StringBuilder to append the extracted hyperlinks.
     */
    private void extractHyperlinks(PDDocument document, StringBuilder result) throws IOException {
        for (PDPage page : document.getPages()) {
            List<PDAnnotation> annotations = page.getAnnotations();

            for (PDAnnotation annot : annotations) {
                if (annot instanceof PDAnnotationLink link) {
                    PDAction action = link.getAction();

                    if (action instanceof PDActionURI uri) {
                        result.append("\n").append(uri.getURI());
                    }
                }
            }
        }
    }
}
