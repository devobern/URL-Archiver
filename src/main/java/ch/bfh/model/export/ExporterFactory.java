package ch.bfh.model.export;

/**
 * Factory class for creating instances of various types of exporters.
 */
public class ExporterFactory {

    /**
     * Returns an exporter instance based on the specified type.
     * Currently supports 'csv' and 'bib' types.
     *
     * @param type the type of exporter to create. Can be 'csv' for CSVExporter or 'bib' for BIBExporter.
     * @return an instance of the specified Exporter type.
     * @throws IllegalArgumentException if the specified exporter type is unknown.
     */
    public static Exporter getExporter(String type) {
        return switch (type) {
            case "csv" -> new CSVExporter();
            case "bib" -> new BIBExporter();
            default -> throw new IllegalArgumentException("Unknown Exporter Type");
        };
    }
}
