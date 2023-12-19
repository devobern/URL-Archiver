package ch.bfh.model.export;

import ch.bfh.model.FileModel;
import ch.bfh.model.URLPair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class BIBExporter implements Exporter {

    /**
     * Exports the bibliographic data to a BibTeX file, updating entries with archived URLs.
     *
     * @param fileModel The FileModel object containing file path and URL pairs.
     * @param destinationPath The path to the destination file.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void exportURLs(FileModel fileModel, String destinationPath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileModel.getFilePath().toString()));
        List<URLPair> urlPairs = fileModel.getUrlPairs();

        for (URLPair pair : urlPairs) {
            String archivedUrls = formatArchivedUrls(pair);
            if (!archivedUrls.isEmpty()) {
                updateBibEntriesWithArchivedUrls(lines, pair.getExtractedURL(), archivedUrls);
            }
        }

        Files.write(Paths.get(destinationPath), lines);
    }

    /**
     * Formats archived URLs for insertion into the BibTeX file.
     *
     * @param pair The URLPair object containing the list of archived URLs.
     * @return A formatted string of archived URLs.
     */
    private static String formatArchivedUrls(URLPair pair) {
        if (pair.getArchivedURLs().isEmpty()) {
            return "";
        }
        return pair.getArchivedURLs().stream()
                .map(url -> "\\url{" + url + "}")
                .collect(Collectors.joining(", ", "Archived Versions: ", ""));
    }

    /**
     * Updates the BibTeX entries with archived URLs.
     *
     * @param lines         The list of lines in the BibTeX file.
     * @param extractedUrl  The URL to match in BibTeX entries.
     * @param archivedUrls  The formatted string of archived URLs.
     */
    private static void updateBibEntriesWithArchivedUrls(List<String> lines, String extractedUrl, String archivedUrls) {
        int entryStart = -1;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (isStartOfBibEntry(line)) {
                entryStart = i;
            } else if (isEndOfBibEntry(line, entryStart)) {
                updateBibEntry(lines, entryStart, i, extractedUrl, archivedUrls);
                entryStart = -1;
            }
        }
    }

    /**
     * Checks if a line marks the start of a BibTeX entry.
     *
     * @param line The line to check.
     * @return true if the line is the start of a BibTeX entry, false otherwise.
     */
    private static boolean isStartOfBibEntry(String line) {
        return line.trim().startsWith("@");
    }

    /**
     * Checks if a line marks the end of a BibTeX entry.
     *
     * @param line        The line to check.
     * @param entryStart  The index of the start of the BibTeX entry.
     * @return true if the line is the end of a BibTeX entry, false otherwise.
     */
    private static boolean isEndOfBibEntry(String line, int entryStart) {
        return entryStart != -1 && line.trim().equals("}");
    }

    /**
     * Updates a specific BibTeX entry with archived URLs.
     *
     * @param lines         The list of lines in the BibTeX file.
     * @param entryStart    The start index of the BibTeX entry.
     * @param entryEnd      The end index of the BibTeX entry.
     * @param extractedUrl  The URL to match in the BibTeX entry.
     * @param archivedUrls  The formatted string of archived URLs.
     */
    private static void updateBibEntry(List<String> lines, int entryStart, int entryEnd, String extractedUrl, String archivedUrls) {
        for (int j = entryStart; j <= entryEnd; j++) {
            if (lines.get(j).contains(extractedUrl)) {
                if (!updateNoteField(lines, j, entryEnd, archivedUrls)) {
                    addNewNoteField(lines, entryEnd, archivedUrls);
                }
                break;
            }
        }
    }

    /**
     * Updates the note field of a BibTeX entry with archived URLs.
     *
     * @param lines        The list of lines in the BibTeX file.
     * @param start        The start index of the BibTeX entry.
     * @param end          The end index of the BibTeX entry.
     * @param archivedUrls The formatted string of archived URLs.
     * @return true if the note field was updated, false otherwise.
     */
    private static boolean updateNoteField(List<String> lines, int start, int end, String archivedUrls) {
        for (int k = start; k <= end; k++) {
            String currentLine = lines.get(k);
            if (currentLine.trim().startsWith("note =")) {
                String updatedNote = appendToNoteField(currentLine, archivedUrls);
                lines.set(k, updatedNote);
                return true;
            }
        }
        return false;
    }

    /**
     * Appends archived URLs to the note field of a BibTeX entry.
     *
     * @param currentLine  The current line of the BibTeX entry.
     * @param archivedUrls The formatted string of archived URLs.
     * @return The updated line with archived URLs appended.
     */
    private static String appendToNoteField(String currentLine, String archivedUrls) {
        char closingChar = currentLine.trim().endsWith("},") ? '}' : '"';
        int closingPos = currentLine.lastIndexOf(closingChar);
        return currentLine.substring(0, closingPos) + ", " + archivedUrls + closingChar;
    }

    /**
     * Adds a new note field to a BibTeX entry.
     *
     * @param lines        The list of lines in the BibTeX file.
     * @param entryEnd     The end index of the BibTeX entry.
     * @param archivedUrls The formatted string of archived URLs.
     */
    private static void addNewNoteField(List<String> lines, int entryEnd, String archivedUrls) {
        String precedingLine = lines.get(entryEnd - 1).trim();
        if (!precedingLine.endsWith(",")) {
            lines.set(entryEnd - 1, precedingLine + ",");
        }
        lines.add(entryEnd, "    note = {" + archivedUrls + "},");
    }
}
