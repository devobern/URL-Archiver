package ch.bfh.model.export;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExporterFactoryTest {

    /**
     * Test that getExporter returns a CSVExporter instance when "csv" type is provided.
     */
    @Test
    void testGetCsvExporter() {
        Exporter exporter = ExporterFactory.getExporter("csv");
        assertInstanceOf(CSVExporter.class, exporter, "getExporter should return a CSVExporter for 'csv' type");
    }

    /**
     * Test that getExporter returns a BIBExporter instance when "bib" type is provided.
     */
    @Test
    void testGetBibExporter() {
        Exporter exporter = ExporterFactory.getExporter("bib");
        assertInstanceOf(BIBExporter.class, exporter, "getExporter should return a BIBExporter for 'bib' type");
    }

    /**
     * Test that getExporter throws an IllegalArgumentException for an unknown exporter type.
     */
    @Test
    void testGetUnknownExporter() {
        assertThrows(IllegalArgumentException.class, () -> ExporterFactory.getExporter("unknown"),
                "getExporter should throw an IllegalArgumentException for unknown types");
    }
}
