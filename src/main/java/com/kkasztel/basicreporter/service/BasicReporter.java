package com.kkasztel.basicreporter.service;

import static java.util.function.Function.identity;

import com.kkasztel.basicreporter.model.Report;
import com.kkasztel.basicreporter.model.ReportDefinition;
import com.kkasztel.basicreporter.model.ReportingException;

import io.vavr.control.Either;

/**
 * Core interface for generating reports from a {@link ReportDefinition}.
 * <p>
 * Implementations produce reports in specific formats (e.g., CSV, XLS, XLSX).
 * Two generation methods are provided:
 * <ul>
 *   <li>{@link #tryGenerate(ReportDefinition)} — returns an {@link Either} for safe, functional error handling</li>
 *   <li>{@link #generate(ReportDefinition)} — returns the report directly or throws on failure</li>
 * </ul>
 *
 * @see com.kkasztel.basicreporter.service.csv.CsvBasicReporter
 * @see com.kkasztel.basicreporter.service.excel.XlsBasicReporter
 * @see com.kkasztel.basicreporter.service.excel.XlsxBasicReporter
 */
public interface BasicReporter {

    /**
     * Attempts to generate a report from the given definition.
     *
     * @param definition the report definition describing the content and structure
     * @return an {@link Either} containing either a {@link ReportingException} (left) on failure,
     *         or the generated {@link Report} (right) on success
     */
    Either<ReportingException, Report> tryGenerate(ReportDefinition definition);

    /**
     * Generates a report from the given definition, throwing on failure.
     *
     * @param definition the report definition describing the content and structure
     * @return the generated report
     * @throws ReportingException if report generation fails
     */
    default Report generate(ReportDefinition definition) {
        return tryGenerate(definition).getOrElseThrow(identity());
    }
}
