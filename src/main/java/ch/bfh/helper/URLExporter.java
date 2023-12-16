package ch.bfh.helper;

import ch.bfh.exceptions.URLExporterException;
import ch.bfh.model.FileModel;
import ch.bfh.model.FolderModel;
import ch.bfh.model.URLPair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class URLExporter {
    static final String delimiter = ";";

    /**
     * exports the archived URLs from a single file into a csv file
     * @param file
     * @param destinationPath the path must be validated and the file extension has to be .csv
     */
    public static void exportUrlsToCSV(FileModel file, String destinationPath) throws FileNotFoundException, URLExporterException {
        ArrayList<String[]> archivedUrls = new ArrayList<>();

        // add header as a first row
        archivedUrls.add(new String[] {"extracted url", "archived url"});

        for (URLPair url : file.getUrlPairs()) {
            if(url.getArchivedURLs() != null && !url.getArchivedURLs().isEmpty()) {
                String archivedUrlsString = String.join(", ", url.getArchivedURLs());
                archivedUrls.add(new String[] {url.getExtractedURL(), archivedUrlsString});
            }
        }

        File csvOutputFile = new File(destinationPath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            archivedUrls.stream()
                    .map(URLExporter::convertToCSV)
                    .forEach(pw::println);
        }
        if (!csvOutputFile.exists()) {
            throw new URLExporterException(I18n.getString("error.exporter"));
        }
    }

    /**
     * exports the archived URLs from a folder into a csv file
     * @param folder
     * @param destinationPath the path must be validated and the file extension has to be .csv
     */
    public static void exportUrlsToCSV(FolderModel folder, String destinationPath) throws URLExporterException, FileNotFoundException {
        ArrayList<String[]> archivedUrls = new ArrayList<>();
        // add header as a first row
        archivedUrls.add(new String[] {"extracted url", "archived url"});

        for (FileModel file : folder.getFiles()) {
            for (URLPair url : file.getUrlPairs()) {
                if(url.getArchivedURLs() != null && !url.getArchivedURLs().isEmpty()) {
                    String archivedUrlsString = String.join(", ", url.getArchivedURLs());
                    archivedUrls.add(new String[] {url.getExtractedURL(), archivedUrlsString});
                }
            }
        }

        File csvOutputFile = new File(destinationPath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            archivedUrls.stream()
                    .map(URLExporter::convertToCSV)
                    .forEach(pw::println);
        }
        if (!csvOutputFile.exists()) {
            throw(new URLExporterException(I18n.getString("error.exporter")));
        }

    }

    public static String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(URLExporter::escapeSpecialCharacters)
                .collect(Collectors.joining(delimiter));
    }

    public static String escapeSpecialCharacters(String data) {
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
