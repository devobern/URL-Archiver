package ch.bfh.model.export;

public class ExporterFactory {
    public static Exporter getExporter(String type) {
        return switch (type) {
            case "csv" -> new CSVExporter();
            case "bib" -> new BIBExporter();
            default -> throw new IllegalArgumentException("Unknown Exporter Type");
        };
    }
}
