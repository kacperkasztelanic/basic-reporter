package com.kkasztel.basicreporter.service.excel;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.kkasztel.basicreporter.model.Report;
import com.kkasztel.basicreporter.model.ReportDefinition;
import static com.kkasztel.basicreporter.model.ReportType.XLSX;

import static io.vavr.API.Try;
import io.vavr.control.Try;

/**
 * Generates reports in Microsoft Excel Open XML Spreadsheet format ({@code .xlsx}).
 * <p>
 * Supports multiple sheets, optional column auto-sizing, and optional cell borders.
 *
 * <h3>Example usage:</h3>
 * <pre>{@code
 * BasicReporter reporter = new XlsxBasicReporter(true, true);
 * Report report = reporter.generate(definition);
 * }</pre>
 */
public class XlsxBasicReporter extends AbstractExcelBasicReporter {

    /**
     * Creates a new XLSX reporter with default settings (no auto-sizing, no borders).
     */
    public XlsxBasicReporter() {
        super(false, false);
    }

    /**
     * Creates a new XLSX reporter with the specified options.
     *
     * @param useAutosize whether to auto-size columns to fit content
     * @param bordered    whether to add thin borders to all cells
     */
    public XlsxBasicReporter(boolean useAutosize, boolean bordered) {
        super(useAutosize, bordered);
    }

    @Override
    public Try<Report> generateReport(ReportDefinition definition) {
        return Try(() -> generateData(definition, true))
                .map(d -> Report.of(definition.getName(), d, XLSX, UTF_8));
    }
}
