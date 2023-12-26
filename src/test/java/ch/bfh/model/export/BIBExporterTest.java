package ch.bfh.model.export;

import ch.bfh.model.FileModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BIBExporterTest {

    @TempDir
    Path tempDir;
    private BIBExporter bibExporter;

    @BeforeEach
    void setUp() {
        bibExporter = new BIBExporter();
    }

    @Test
    void testExportURLsWithExtractedUrlPresent() throws IOException {
        Path sourcePath = tempDir.resolve("biblatex-examples.bib");
        String bibEntry = "@online{ctan,\n" +
                "  title        = {CTAN},\n" +
                "  date         = 2006,\n" +
                "  url          = {http://www.ctan.org},\n" +
                "  subtitle     = {The {Comprehensive TeX Archive Network}},\n" +
                "  urldate      = {2006-10-01},\n" +
                "  label        = {CTAN},\n" +
                "  langid       = {english},\n" +
                "  langidopts   = {variant=american},\n" +
                "  annotation   = {This is an \\texttt{online} entry. The \\textsc{url}, which is\n" +
                "                  given in the \\texttt{url} field, is transformed into a\n" +
                "                  clickable link if \\texttt{hyperref} support has been\n" +
                "                  enabled. Note the format of the \\texttt{urldate} field\n" +
                "                  (\\texttt{yyyy-mm-dd}) in the database file. Also note the\n" +
                "                  \\texttt{label} field which may be used as a fallback by\n" +
                "                  citation styles which need an \\texttt{author} and\\slash or a\n" +
                "                  \\texttt{year}},\n" +
                "}";
        Files.writeString(sourcePath, bibEntry);

        FileModel fileModel = new FileModel(sourcePath, "text/plain");
        fileModel.addExtractedURLs(Set.of("http://www.ctan.org"));
        fileModel.addArchivedURL("http://www.ctan.org", "http://archive.org/example");

        bibExporter.exportURLs(fileModel, sourcePath.toString());

        List<String> lines = Files.readAllLines(sourcePath);
        boolean urlAdded = lines.stream().anyMatch(line -> line.contains("http://archive.org/example"));
        assertTrue(urlAdded, "Archived URL should be added to the note field for the matching extracted URL");
    }

    @Test
    void testExportURLsWithExtractedUrlPresentAndNoteField() throws IOException {
        Path sourcePath = tempDir.resolve("biblatex-examples.bib");
        String bibEntry = "@online{ctan,\n" +
                "  title        = {CTAN},\n" +
                "  date         = 2006,\n" +
                "  url          = {http://www.ctan.org},\n" +
                "  subtitle     = {The {Comprehensive TeX Archive Network}},\n" +
                "  urldate      = {2006-10-01},\n" +
                "  label        = {CTAN},\n" +
                "  langid       = {english},\n" +
                "  langidopts   = {variant=american},\n" +
                "  annotation   = {Existing note content},\n" +
                "  note         = {Ed. facs. de la realizada en 1948--49},\n" +
                "}";
        Files.writeString(sourcePath, bibEntry);

        FileModel fileModel = new FileModel(sourcePath, "text/plain");
        fileModel.addExtractedURLs(Collections.singleton("http://www.ctan.org"));
        fileModel.addArchivedURL("http://www.ctan.org", "http://archive.org/example");

        bibExporter.exportURLs(fileModel, sourcePath.toString());

        List<String> lines = Files.readAllLines(sourcePath);
        boolean urlAdded = lines.stream().anyMatch(line -> line.contains("http://archive.org/example"));
        assertTrue(urlAdded, "Archived URL should be added to the existing note field for the matching extracted URL");
    }


    @Test
    void testExportURLsWithExtractedUrlAbsent() throws IOException {
        Path sourcePath = tempDir.resolve("biblatex-examples.bib");
        String bibEntry = "@article{example, title={Example Title}, url={http://nonexistent.com}}";
        Files.writeString(sourcePath, bibEntry);

        FileModel fileModel = new FileModel(sourcePath, "text/plain");
        fileModel.addExtractedURLs(Set.of("http://example.com"));
        fileModel.addArchivedURL("http://example.com", "http://archive.org/example");

        bibExporter.exportURLs(fileModel, sourcePath.toString());

        List<String> lines = Files.readAllLines(sourcePath);
        boolean urlUnchanged = lines.stream().noneMatch(line -> line.contains("http://archive.org/example"));
        assertTrue(urlUnchanged, "BibTeX entry should remain unchanged if extracted URL is not present");
    }
}
