package com.kkasztel.basicreporter.service.csv;

import com.kkasztel.basicreporter.model.Report;
import com.kkasztel.basicreporter.model.ReportDefinition;
import com.kkasztel.basicreporter.model.ReportDefinition.Table;
import com.kkasztel.basicreporter.model.ReportingException;
import com.kkasztel.basicreporter.service.BasicReporter;
import com.kkasztel.basicreporter.service.csv.padding.CellFormatStrategy;
import com.kkasztel.basicreporter.service.csv.padding.CellFormatStrategyFactory;
import io.vavr.control.Either;

import java.nio.charset.Charset;

import static com.kkasztel.basicreporter.model.ReportType.CSV;
import static io.vavr.API.Try;

/**
 * Generates reports in CSV (or tab-separated) text format.
 * <p>
 * This reporter supports configurable field separators, line separators, and character encodings.
 * When using a tab ({@code \t}) as the separator, column values are automatically padded with
 * spaces for visual alignment.
 * <p>
 * Only single-sheet report definitions are supported. Attempting to generate a report from a
 * multi-sheet definition will result in a {@link ReportingException}.
 *
 * <h2>Example usage:</h2>
 * <pre>{@code
 * BasicReporter reporter = new CsvBasicReporter(",", System.lineSeparator(), StandardCharsets.UTF_8);
 * Report report = reporter.generate(definition);
 * }</pre>
 */
public class CsvBasicReporter implements BasicReporter {

    private final String separator;
    private final String lineSeparator;
    private final Charset charset;

    /**
     * Creates a new CSV reporter with the specified configuration.
     *
     * @param separator     the field separator (e.g., {@code ","} for CSV or {@code "\t"} for TSV)
     * @param lineSeparator the line separator (e.g., {@link System#lineSeparator()})
     * @param charset       the character encoding for the output
     */
    public CsvBasicReporter(String separator, String lineSeparator, Charset charset) {
        this.separator = separator;
        this.lineSeparator = lineSeparator;
        this.charset = charset;
    }

    @Override
    public Either<ReportingException, Report> tryGenerate(ReportDefinition definition) {
        return Try(() -> doGenerate(definition))
                .toEither()
                .mapLeft(t -> new ReportingException(t.getMessage(), t));
    }

    private Report doGenerate(ReportDefinition definition) {
        if (definition.getSheets().size() > 1) {
            throw new IllegalArgumentException("Creation of csv file with multiple sheets is not possible");
        }
        return prepare(definition);
    }

    private Report prepare(ReportDefinition definition) {
        return Report.of(
                definition.getSheets().head().getName(),
                prepareData(definition.getSheets().head().getData()),
                CSV,
                charset
        );
    }

    private byte[] prepareData(Table data) {
        CellFormatStrategy formatStrategy = CellFormatStrategyFactory.createCellFormatStrategy(separator, data);
        return data.getData().prepend(data.getTitleRow())
                .map(r -> r.zipWithIndex().map(p -> formatStrategy.format(p._1, p._2)))
                .map(r -> r.mkString(separator))
                .filter(s -> !s.trim().isEmpty())
                .mkString(lineSeparator)
                .getBytes(charset);
    }
}
