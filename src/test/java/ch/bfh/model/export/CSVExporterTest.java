package ch.bfh.model.export;

import ch.bfh.model.FileModel;
import ch.bfh.model.FolderModel;
import ch.bfh.model.URLPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CSVExporter} class.
 */
class CSVExporterTest {

    @TempDir
    Path tempDir;
    private CSVExporter csvExporter;

    @BeforeEach
    void setUp() {
        csvExporter = new CSVExporter();
    }

    /**
     * Creates a FileModel with no URLs for testing.
     *
     * @return FileModel with no URLs.
     */
    private FileModel createEmptyFileModel() {
        Path filePath = tempDir.resolve("empty_file.txt");
        return new FileModel(filePath, "text/plain");
    }

    /**
     * Creates a FileModel with a single archived URL for testing.
     *
     * @return FileModel with one archived URL.
     */
    private FileModel createSingleArchivedUrlFileModel() {
        Path filePath = tempDir.resolve("single_url_file.txt");
        FileModel fileModel = new FileModel(filePath, "text/plain");
        fileModel.addExtractedURLs(Set.of("http://example.com"));
        fileModel.addArchivedURL("http://example.com", "http://archive.org/example");
        return fileModel;
    }

    /**
     * Creates a FileModel with multiple archived URLs for testing.
     *
     * @return FileModel with multiple archived URLs.
     */
    private FileModel createMultipleArchivedUrlsFileModel() {
        Path filePath = tempDir.resolve("multiple_urls_file.txt");
        FileModel fileModel = new FileModel(filePath, "text/plain");
        fileModel.addExtractedURLs(Set.of("http://example.com"));
        fileModel.setArchivedURL("http://example.com", List.of("http://archive.org/example1", "http://archive.org/example2"));
        return fileModel;
    }

    private List<URLPair> getAllURLPairs(FolderModel folderModel) {
        return folderModel.getFiles().stream().flatMap(fileModel -> fileModel.getUrlPairs().stream()).collect(Collectors.toList());
    }

    private String constructExpectedHeader(int numArchivedUrls) {
        return "extracted url" + IntStream.rangeClosed(1, numArchivedUrls).mapToObj(i -> ";archived url " + i).collect(Collectors.joining());
    }

    private String constructExpectedLine(URLPair pair, int numArchivedUrls) {
        String line = pair.getExtractedURL();
        List<String> archivedUrls = pair.getArchivedURLs();
        return line + IntStream.range(0, numArchivedUrls).mapToObj(i -> ";" + (i < archivedUrls.size() ? archivedUrls.get(i) : "")).collect(Collectors.joining());
    }

    /**
     * Asserts the content of the generated CSV file against the expected format.
     *
     * @param csvPath         the path to the CSV file
     * @param urlPairs        the URLPairs used for generating the CSV content
     * @param numArchivedUrls the number of archived URL columns expected in the header
     * @throws Exception if there is an error reading the file
     */
    private void assertCSVContent(Path csvPath, List<URLPair> urlPairs, int numArchivedUrls) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath.toFile()))) {
            String header = reader.readLine();
            String expectedHeader = constructExpectedHeader(numArchivedUrls);
            assertEquals(expectedHeader, header, "Header row should match expected format with correct number of archived URLs");

            for (URLPair pair : urlPairs) {
                String expectedLine = constructExpectedLine(pair, numArchivedUrls);
                String actualLine = reader.readLine();
                assertEquals(expectedLine, actualLine, "CSV line content should match expected URLPair with correct number of archived URLs");
            }

            assertNull(reader.readLine(), "No more lines expected in CSV file");
        }
    }

    /**
     * Asserts that a generated CSV file is empty except for the header.
     *
     * @param csvPath the path to the CSV file
     * @throws Exception if there is an error reading the file
     */
    private void assertEmptyCSV(Path csvPath) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath.toFile()))) {
            String header = reader.readLine();
            String expectedHeader = "extracted url";
            assertEquals(expectedHeader, header, "CSV header should match expected format");

            assertNull(reader.readLine(), "No data rows expected in CSV file");
        }
    }

    /**
     * Tests the creation of a CSV file for a FileModel with a single archived URL.
     */
    @Test
    void testCSVFileCreationForFileModelSingleArchivedUrl() throws Exception {
        FileModel fileModel = createSingleArchivedUrlFileModel();
        Path csvPath = tempDir.resolve("export_file_single.csv");
        csvExporter.exportURLs(fileModel, csvPath.toString());

        assertTrue(Files.exists(csvPath), "CSV file should be created for FileModel with a single archived URL");
    }

    /**
     * Tests the creation of a CSV file for a FileModel with multiple archived URLs.
     */
    @Test
    void testCSVFileCreationForFileModelMultipleArchivedUrls() throws Exception {
        FileModel fileModel = createMultipleArchivedUrlsFileModel();
        Path csvPath = tempDir.resolve("export_file_multiple.csv");
        csvExporter.exportURLs(fileModel, csvPath.toString());

        assertTrue(Files.exists(csvPath), "CSV file should be created for FileModel with multiple archived URLs");
    }

    /**
     * Tests the content of a CSV file for a FileModel with a single archived URL.
     */
    @Test
    void testCSVContentForFileModelSingleArchivedUrl() throws Exception {
        FileModel fileModel = createSingleArchivedUrlFileModel();
        Path csvPath = tempDir.resolve("export_file_single.csv");
        csvExporter.exportURLs(fileModel, csvPath.toString());

        assertCSVContent(csvPath, fileModel.getUrlPairs(), 1);
    }

    /**
     * Tests the content of a CSV file for a FileModel with multiple archived URLs.
     */
    @Test
    void testCSVContentForFileModelMultipleArchivedUrls() throws Exception {
        FileModel fileModel = createMultipleArchivedUrlsFileModel();
        Path csvPath = tempDir.resolve("export_file_multiple.csv");
        csvExporter.exportURLs(fileModel, csvPath.toString());

        assertCSVContent(csvPath, fileModel.getUrlPairs(), 2);
    }

    /**
     * Tests the creation of a CSV file for a FolderModel with no files.
     */
    @Test
    void testExportURLsFolderModelNoFiles() throws Exception {
        FolderModel folderModel = new FolderModel(tempDir.toString());
        Path csvPath = tempDir.resolve("export_folder_no_files.csv");
        csvExporter.exportURLs(folderModel, csvPath.toString());

        assertTrue(Files.exists(csvPath), "CSV file should be created for FolderModel with no files");
        assertEmptyCSV(csvPath);
    }

    /**
     * Tests the creation of a CSV file for a FileModel with no URLs.
     */
    @Test
    void testExportURLsFileModelNoUrls() throws Exception {
        FileModel fileModel = createEmptyFileModel();
        Path csvPath = tempDir.resolve("export_file_no_urls.csv");
        csvExporter.exportURLs(fileModel, csvPath.toString());

        assertTrue(Files.exists(csvPath), "CSV file should be created for FileModel with no URLs");
        assertEmptyCSV(csvPath);
    }

    /**
     * Tests the creation of a CSV file for a FolderModel with mixed archived URL counts.
     */
    @Test
    void testCSVFileCreationFolderModelMixedArchivedUrls() throws Exception {
        FolderModel folderModel = new FolderModel(tempDir.toString());
        folderModel.addFile(createSingleArchivedUrlFileModel());
        folderModel.addFile(createMultipleArchivedUrlsFileModel());
        Path csvPath = tempDir.resolve("export_folder_mixed.csv");
        csvExporter.exportURLs(folderModel, csvPath.toString());

        assertTrue(Files.exists(csvPath), "CSV file should be created for FolderModel with mixed archived URL counts");
    }

    /**
     * Tests the content of a CSV file for a FolderModel with mixed archived URL counts.
     */
    @Test
    void testCSVContentFolderModelMixedArchivedUrls() throws Exception {
        FolderModel folderModel = new FolderModel(tempDir.toString());
        folderModel.addFile(createSingleArchivedUrlFileModel());
        folderModel.addFile(createMultipleArchivedUrlsFileModel());
        Path csvPath = tempDir.resolve("export_folder_mixed.csv");
        csvExporter.exportURLs(folderModel, csvPath.toString());

        assertCSVContent(csvPath, getAllURLPairs(folderModel), 2);
    }

    /**
     * Tests the creation of a CSV file for a FolderModel containing a single FileModel.
     */
    @Test
    void testCSVFileCreationFolderModelSingleFile() throws Exception {
        FolderModel folderModel = new FolderModel(tempDir.toString());
        folderModel.addFile(createSingleArchivedUrlFileModel());
        Path csvPath = tempDir.resolve("export_folder_single_file.csv");
        csvExporter.exportURLs(folderModel, csvPath.toString());

        assertTrue(Files.exists(csvPath), "CSV file should be created for FolderModel with a single file");
    }

    /**
     * Tests the content of a CSV file for a FolderModel containing a single FileModel.
     */
    @Test
    void testCSVContentFolderModelSingleFile() throws Exception {
        FolderModel folderModel = new FolderModel(tempDir.toString());
        folderModel.addFile(createSingleArchivedUrlFileModel());
        Path csvPath = tempDir.resolve("export_folder_single_file.csv");
        csvExporter.exportURLs(folderModel, csvPath.toString());

        assertCSVContent(csvPath, getAllURLPairs(folderModel), 1);
    }
}
