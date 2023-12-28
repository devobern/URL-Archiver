package ch.bfh.model.filereader;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * FileReader implementation for PDF files.
 * It extracts text and hyperlinks from PDF documents.
 */
public class PDFFileReader implements FileReaderInterface {

    /**
     * Reads and extracts text and hyperlinks from a PDF file specified by the path.
     *
     * @param filePath Path to the PDF file.
     * @return Extracted text and hyperlinks as a String.
     * @throws IOException if an error occurs during file reading or processing.
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
     * Extracts and appends hyperlinks from the PDF document to the StringBuilder.
     *
     * @param document The PDF document from which to extract hyperlinks.
     * @param result   StringBuilder to append the extracted hyperlinks.
     * @throws IOException if an error occurs during hyperlink extraction.
     */
    private void extractHyperlinks(PDDocument document, StringBuilder result) throws IOException {
        for (PDPage page : document.getPages()) {
            List<PDAnnotation> annotations = page.getAnnotations();
            appendAnnotationsHyperlinks(annotations, result);
        }
    }

    /**
     * Appends hyperlinks from a list of annotations to the StringBuilder.
     *
     * @param annotations List of annotations to process for hyperlinks.
     * @param result      StringBuilder to append the extracted hyperlinks.
     */
    private void appendAnnotationsHyperlinks(List<PDAnnotation> annotations, StringBuilder result) {
        for (PDAnnotation annotation : annotations) {
            if (annotation instanceof PDAnnotationLink link && link.getAction() instanceof PDActionURI uri) {
                result.append("\n").append(uri.getURI());
            }
        }
    }
}
