package ch.bfh.model.filereader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link PDFFileReader} class.
 */
class PDFFileReaderTest {

    @TempDir
    Path tempDir;

    /**
     * Verifies text extraction from a PDF file.
     * Ensures that the expected text is present in the content extracted from the PDF.
     *
     * @throws IOException if an error occurs during file creation or reading.
     */
    @Test
    @Disabled("There is a font realated issue on Fedora.")
    @DisplayName("Extract text from PDF file")
    void testReadFileExtractText() throws IOException {
        Path testPdf = createTestPdf("Sample Text");
        PDFFileReader reader = new PDFFileReader();

        String content = reader.readFile(testPdf);

        assertTrue(content.contains("Sample Text"), "Extracted content should contain 'Sample Text'");
    }

    /**
     * Verifies hyperlink extraction from a PDF file.
     * Confirms that the expected hyperlink URL is present in the content extracted from the PDF.
     *
     * @throws IOException if an error occurs during file creation or reading.
     */
    @Test
    @Disabled("There is a font realated issue on Fedora.")
    @DisplayName("Extract hyperlinks from PDF file")
    void testReadFileExtractHyperlinks() throws IOException {
        Path testPdf = createTestPdfWithHyperlink("http://example.com");
        PDFFileReader reader = new PDFFileReader();

        String content = reader.readFile(testPdf);

        assertTrue(content.contains("http://example.com"), "Extracted content should contain the hyperlink");
    }

    /**
     * Ensures proper handling of IOException for unreadable PDF files.
     * Tests the scenario where the provided file path is invalid.
     */
    @Test
    @DisplayName("Handle IOException for unreadable PDF")
    void testReadFileIOException() {
        Path invalidPath = tempDir.resolve("nonexistent.pdf");
        PDFFileReader reader = new PDFFileReader();

        assertThrows(IOException.class, () -> reader.readFile(invalidPath),
                "Should throw IOException for unreadable PDF");
    }

    private Path createTestPdf(String text) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
        contentStream.newLineAtOffset(50, 750);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.close();

        Path pdfPath = tempDir.resolve("sample.pdf");
        document.save(pdfPath.toFile());
        document.close();

        return pdfPath;
    }

    private Path createTestPdfWithHyperlink(String url) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
        contentStream.newLineAtOffset(50, 750);
        contentStream.showText("Click here for hyperlink");
        contentStream.endText();
        contentStream.close();

        // Create a hyperlink annotation
        PDAnnotationLink link = new PDAnnotationLink();
        PDActionURI action = new PDActionURI();
        action.setURI(url);
        link.setAction(action);

        // Set the annotation rectangle
        PDRectangle position = new PDRectangle();
        position.setLowerLeftX(50);
        position.setLowerLeftY(740);
        position.setUpperRightX(200);
        position.setUpperRightY(760);
        link.setRectangle(position);

        page.getAnnotations().add(link);

        Path pdfPath = tempDir.resolve("hyperlink.pdf");
        document.save(pdfPath.toFile());
        document.close();

        return pdfPath;
    }
}
