package com.kkasztel.basicreporter.service.excel;

import com.kkasztel.basicreporter.model.Report;
import com.kkasztel.basicreporter.model.ReportDefinition;
import io.vavr.control.Try;

import static com.kkasztel.basicreporter.model.ReportType.XLS;
import static io.vavr.API.Try;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Generates reports in Microsoft Excel Binary File Format ({@code .xls}).
 * <p>
 * Supports multiple sheets, optional column auto-sizing, and optional cell borders.
 *
 * <h3>Example usage:</h3>
 * <pre>{@code
 * BasicReporter reporter = new XlsBasicReporter(true, true);
 * Report report = reporter.generate(definition);
 * }</pre>
 */
public class XlsBasicReporter extends AbstractExcelBasicReporter {

    /**
     * Creates a new XLS reporter with default settings (no auto-sizing, no borders).
     */
    public XlsBasicReporter() {
        super(false, false);
    }

    /**
     * Creates a new XLS reporter with the specified options.
     *
     * @param useAutosize whether to auto-size columns to fit content
     * @param bordered    whether to add thin borders to all cells
     */
    public XlsBasicReporter(boolean useAutosize, boolean bordered) {
        super(useAutosize, bordered);
    }

    @Override
    public Try<Report> generateReport(ReportDefinition definition) {
        return Try(() -> generateData(definition, false))
                .map(d -> Report.of(definition.getName(), d, XLS, UTF_8));
    }
}
