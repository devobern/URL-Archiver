package ch.bfh.model.export;

import ch.bfh.exceptions.URLExporterException;
import ch.bfh.model.FileModel;
import ch.bfh.model.FolderModel;

import java.io.IOException;

/**
 * Interface for exporting URL data to different file formats.
 */
public interface Exporter {

    /**
     * Exports URLs from a FileModel to a specified destination.
     *
     * @param fileModel       The FileModel containing the URLs to be exported.
     * @param destinationPath The path where the exported file will be saved.
     * @throws IOException          If an I/O error occurs during exporting.
     * @throws URLExporterException If any URL-specific export exception occurs.
     */
    void exportURLs(FileModel fileModel, String destinationPath) throws IOException, URLExporterException;

    /**
     * Exports URLs from a FolderModel to a specified destination.
     *
     * @param folderModel     The FolderModel containing the URLs to be exported.
     * @param destinationPath The path where the exported file will be saved.
     * @throws IOException          If an I/O error occurs during exporting.
     * @throws URLExporterException If any URL-specific export exception occurs.
     */
    void exportURLs(FolderModel folderModel, String destinationPath) throws IOException, URLExporterException;
}
