package ch.bfh.model.export;

import ch.bfh.exceptions.URLExporterException;
import ch.bfh.helper.I18n;
import ch.bfh.model.FileModel;
import ch.bfh.model.FolderModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVExporter implements Exporter {
    final String delimiter = ";";

    /**
     * Exports the URLs of the given file to a CSV file.
     *
     * @param fileModel            the file to export the URLs from
     * @param destinationPath the path to the CSV file
     * @throws FileNotFoundException if the file could not be created
     * @throws URLExporterException   if the file could not be created
     */
    @Override
    public void exportURLs(FileModel fileModel, String destinationPath) throws IOException, URLExporterException {
        int maxArchivedUrls = getMaxArchivedUrls(Stream.of(fileModel));
        List<String[]> dataRows = createDataRows(Stream.of(fileModel), maxArchivedUrls);
        writeCSV(dataRows, destinationPath);
    }

    public void exportURLs(FolderModel folderModel, String destinationPath) throws IOException, URLExporterException {
        int maxArchivedUrls = getMaxArchivedUrls(folderModel.getFiles().stream());
        List<String[]> dataRows = createDataRows(folderModel.getFiles().stream(), maxArchivedUrls);
        writeCSV(dataRows, destinationPath);
    }

    /**
     * Calculates the maximum number of archived URLs across all URL pairs in a given stream of FileModels.
     *
     * @param fileModelStream The stream of FileModel objects to be processed.
     * @return The maximum count of archived URLs found in any single URLPair.
     */
    private int getMaxArchivedUrls(Stream<FileModel> fileModelStream) {
        return fileModelStream
                .flatMap(file -> file.getUrlPairs().stream())
                .mapToInt(urlPair -> urlPair.getArchivedURLs() != null ? urlPair.getArchivedURLs().size() : 0)
                .max()
                .orElse(0);
    }

    /**
     * Creates a list of data rows for CSV export, based on the provided stream of FileModels.
     * Each row in the list represents a URLPair, with columns for the extracted URL and each archived URL.
     *
     * @param fileModelStream The stream of FileModel objects to be processed.
     * @param maxArchivedUrls The maximum number of archived URLs for any URLPair, used to determine the number of columns.
     * @return A list of string arrays, where each array represents a row in the CSV output.
     */
    private List<String[]> createDataRows(Stream<FileModel> fileModelStream, int maxArchivedUrls) {
        ArrayList<String[]> dataRows = new ArrayList<>();
        // Header
        List<String> headers = new ArrayList<>(Collections.singletonList("extracted url"));
        for (int i = 1; i <= maxArchivedUrls; i++) {
            headers.add("archived url " + i);
        }
        dataRows.add(headers.toArray(new String[0]));

        // Data rows
        fileModelStream
                .flatMap(file -> file.getUrlPairs().stream())
                .filter(urlPair -> urlPair.getArchivedURLs() != null && !urlPair.getArchivedURLs().isEmpty()) // Filter out URLPairs without archived URLs
                .forEach(urlPair -> {
                    List<String> row = new ArrayList<>();
                    row.add(urlPair.getExtractedURL());
                    if(urlPair.getArchivedURLs() != null) {
                        row.addAll(urlPair.getArchivedURLs());
                    }
                    // Fill the remaining columns
                    while(row.size() < headers.size()) {
                        row.add("");
                    }
                    dataRows.add(row.toArray(new String[0]));
                });

        return dataRows;
    }

    /**
     * Writes the provided data rows to a CSV file at the specified destination path.
     *
     * @param data The data rows to be written to the CSV file.
     * @param destinationPath The file path where the CSV file will be created.
     * @throws FileNotFoundException if the file cannot be created.
     * @throws URLExporterException if there is an error during the writing process.
     */
    private void writeCSV(List<String[]> data, String destinationPath) throws FileNotFoundException, URLExporterException {
        File csvOutputFile = new File(destinationPath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            data.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
        if (!csvOutputFile.exists()) {
            throw new URLExporterException(I18n.getString("error.exporter"));
        }
    }

    /**
     * Converts an array of data into a single CSV-formatted string.
     *
     * @param data The array of strings to be joined into a CSV row.
     * @return A string representing a row in CSV format.
     */
    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(delimiter));
    }

    /**
     * Escapes special characters in a string to ensure CSV format compliance.
     * Special characters include line breaks, commas, and quotes.
     *
     * @param data The string to be escaped.
     * @return The escaped string, suitable for inclusion in a CSV file.
     * @throws IllegalArgumentException if the input data is null.
     */
    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
