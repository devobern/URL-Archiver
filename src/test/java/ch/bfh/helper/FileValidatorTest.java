package ch.bfh.helper;

import ch.bfh.exceptions.FileModelException;
import ch.bfh.exceptions.PathValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class FileValidatorTest {

    @BeforeEach
    void setUp() {

        I18n.getResourceBundle(Locale.forLanguageTag("en-US"));

        // create a test text file (is supported) for testing
        try {
            FileWriter myWriter = new FileWriter("testTextFileValidatorTest.txt");
            myWriter.write("This is a test file");
            myWriter.close();
            System.out.println("Test Text-File created successfully.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }

        // create an unsupported file (a png image file) for testing
        int width = 400;
        int height = 200;

        // Create a BufferedImage with the desired width and height
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Fill the image with a white background
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, 0xFFFFFFFF); // White color
            }
        }

        // Create a file to save the image
        File outputImageFile = new File("testImageFileValidatorTest.png");

        try {
            // Write the BufferedImage to the file as a PNG image
            ImageIO.write(image, "png", outputImageFile);
            System.out.println("Test PNG image created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create an empty folder for testing
        Path folderPath = Paths.get("testFolderFileValidatorTest");
        try {
            Files.createDirectory(folderPath);
            System.out.println("Test Folder created successfully.");
        } catch (IOException e) {
            System.err.println("Failed to create folder: " + e.getMessage());
        }


    }

    @AfterEach
    void tearDown() {
        // delete test folder
        File folder = new File("testFolderFileValidatorTest");
        folder.delete();

        // delete test text file
        File file = new File("testTextFileValidatorTest.txt");
        file.delete();

        // delete test image file
        File image = new File("testImageFileValidatorTest.png");
        image.delete();

    }

    @Test
    void validate() {
        FileValidator fileValidator = new FileValidator();

        // test if mimeType of text file is detected
        String mimeType = "";
        try {
            mimeType = fileValidator.validate("testTextFileValidatorTest.txt");
        } catch (FileModelException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        assertEquals("text/plain", mimeType);

        // test FileModelExceptions
        FileModelException notFileException = assertThrows(FileModelException.class, () -> {fileValidator.validate("testFolderFileValidatorTest");});
        FileModelException notSupportedException = assertThrows(FileModelException.class, () -> {fileValidator.validate("testImageFileValidatorTest.png");});

        // test if the correct exception messages are thrown for FileModelExceptions
        assertEquals(I18n.getString("file.isNotFile.error"), notFileException.getMessage());
        assertEquals(I18n.getString("file.notSupported.error"), notSupportedException.getMessage());

    }
}