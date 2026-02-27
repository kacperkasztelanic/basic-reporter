package com.kkasztel.basicreporter.model;

/**
 * Enumerates the supported report output formats.
 */
public enum ReportType {

    /**
     * Comma-separated values format ({@code .csv}).
     */
    CSV,
    /**
     * Microsoft Excel Open XML Spreadsheet format ({@code .xlsx}).
     */
    XLSX,
    /**
     * Microsoft Excel Binary File Format ({@code .xls}).
     */
    XLS;

    /**
     * Returns the file extension for this report type.
     *
     * @return the lowercase file extension (e.g., {@code "csv"}, {@code "xlsx"}, {@code "xls"})
     */
    public String getExtension() {
        return name().toLowerCase();
    }
}
