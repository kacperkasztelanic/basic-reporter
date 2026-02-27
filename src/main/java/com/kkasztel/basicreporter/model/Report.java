package com.kkasztel.basicreporter.model;

import java.nio.charset.Charset;

import lombok.Value;

/**
 * Represents a generated report containing the report content as a byte array,
 * along with metadata such as the report name, type, and character encoding.
 * <p>
 * Use the static factory method
 * {@link #of(String, byte[], ReportType, Charset)} to create instances.
 */
@Value(staticConstructor = "of")
public class Report {

    /**
     * The base name of the report (without file extension).
     */
    String name;
    /**
     * The raw content of the report as a byte array.
     */
    byte[] bytes;
    /**
     * The format of the report (e.g., CSV, XLS, XLSX).
     */
    ReportType type;
    /**
     * The character encoding used for the report content.
     */
    Charset charset;

    /**
     * Returns the full file name of the report, including the extension derived
     * from the report type.
     *
     * @return the report name with extension, e.g. {@code "report.csv"}
     */
    public String getFullName() {
        return String.format("%s.%s", name, type.getExtension());
    }

    @Override
    public String toString() {
        return String.format("Report(%s, %s)", getFullName(), charset.displayName());
    }
}
