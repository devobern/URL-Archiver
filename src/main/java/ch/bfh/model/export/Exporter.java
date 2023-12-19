package ch.bfh.model.export;

import ch.bfh.model.FileModel;

import java.io.IOException;

public interface Exporter {
    void exportURLs(FileModel fileModel, String destinationPath) throws IOException;
}
