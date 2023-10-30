package ch.bfh.model;

import ch.bfh.helper.I18n;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class FileModelTest {

    @BeforeEach
    void setUp() {
        I18n.getResourceBundle(Locale.forLanguageTag("en-US"));

        // create a test text file (is supported) for testing
        try {
            FileWriter myWriter = new FileWriter("testTextFileModelTest.txt");
            myWriter.write("This is a test file");
            myWriter.close();
            System.out.println("Test Text-File created successfully.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }

        // create a test pdf file (is supported) for testing
        try {
            // Create a new PDF document
            PDDocument document = new PDDocument();

            // Create a new page
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a content stream for adding content to the page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Set the font and font size
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);

            // Add text to the page
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("This is a pdf test file");
            contentStream.endText();

            // Close the content stream
            contentStream.close();

            // Save the PDF to a file
            document.save("testPDFFileModelTest.pdf");

            // Close the document
            document.close();

            System.out.println("PDF created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() {
        // delete test text file
        File file = new File("testTextFileModelTest.txt");
        file.delete();

        // delete test pdf file
        File pdffile = new File("testPDFFileModelTest.pdf");
        pdffile.delete();
    }

    @Test
    void fileToString() {
        FileModel file = new FileModel("testTextFileModelTest.txt", "text/plain");
        FileModel pdfFile = new FileModel("testPDFFileModelTest.pdf", "application/pdf");

        try {
            assertEquals("This is a test file", file.fileToString());
            assertTrue(pdfFile.fileToString().contains("This is a pdf test file"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}