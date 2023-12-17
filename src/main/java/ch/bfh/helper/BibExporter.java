package ch.bfh.helper;

import ch.bfh.model.FileModel;
import ch.bfh.model.URLPair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class BibExporter {

    public static void exportToBib(FileModel fm) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fm.getFilePath().toString()));
        List<URLPair> urlPairs = fm.getUrlPairs();

        for (URLPair pair : urlPairs) {
            String extractedUrl = pair.getExtractedURL();
            String archivedUrls = pair.getArchivedURLs().stream()
                    .map(url -> "\\url{" + url + "}")
                    .collect(Collectors.joining(", "));
            String archiveText = archivedUrls.isEmpty() ? "" : "Archived Versions: " + archivedUrls;

            if (!archivedUrls.isEmpty()) {
                boolean noteUpdated = false;
                int entryStart = -1, entryEnd = -1;
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);

                    // Check for start of a BibTeX entry
                    if (line.trim().startsWith("@")) {
                        entryStart = i;
                        entryEnd = -1; // Reset end of entry
                    }

                    // Check for the end of the entry
                    if (entryStart != -1 && line.trim().equals("}")) {
                        entryEnd = i;

                        // Check if the entry contains the extracted URL
                        for (int j = entryStart; j <= entryEnd; j++) {
                            if (lines.get(j).contains(extractedUrl)) {
                                // Entry with URL found, update or add the note
                                for (int k = j; k <= entryEnd; k++) {
                                    String currentLine = lines.get(k);

                                    if (currentLine.trim().startsWith("note =")) {
                                        // Find the closing character of the note field
                                        char closingChar = currentLine.trim().endsWith("},") ? '}' : '"';
                                        int closingPos = currentLine.lastIndexOf(closingChar);

                                        // Insert archived URL text before the closing character
                                        String updatedNote = currentLine.substring(0, closingPos) + ", " + archiveText + closingChar;
                                        lines.set(k, updatedNote);
                                        noteUpdated = true;
                                        break;
                                    }
                                }

                                // If note field not found, add a new note field at the end of the entry
                                if (!noteUpdated) {
                                    // Ensure the preceding line ends with a comma
                                    String precedingLine = lines.get(entryEnd - 1).trim();
                                    if (!precedingLine.endsWith(",")) {
                                        lines.set(entryEnd - 1, precedingLine + ",");
                                    }
                                    String newNoteField = "    note = {" + archiveText + "},";
                                    lines.add(entryEnd, newNoteField);
                                }

                                break; // Break out of the loop once the entry is processed
                            }
                        }
                    }
                }
            }
        }
        // Write the modified content back to the BIB file
        Files.write(Paths.get(fm.getFilePath().toString()), lines);
    }
}
