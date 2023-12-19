package ch.bfh.model.export;

import ch.bfh.exceptions.URLExporterException;
import ch.bfh.model.FileModel;

import java.io.IOException;

public interface Exporter {
    void exportURLs(FileModel fileModel, String destinationPath) throws IOException, URLExporterException;
}
