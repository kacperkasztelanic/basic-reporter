package com.kkasztel.basicreporter.service.csv;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.apache.commons.io.IOUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import com.kkasztel.basicreporter.model.Report;
import com.kkasztel.basicreporter.model.ReportDefinition;
import static com.kkasztel.basicreporter.model.ReportType.CSV;
import com.kkasztel.basicreporter.model.ReportingException;
import com.kkasztel.basicreporter.service.BasicReporter;
import com.kkasztel.basicreporter.service.TestDataProvider;

import io.vavr.control.Either;
import io.vavr.control.Try;

class CsvBasicReporterTest {

    @Test
    void successfullyGeneratesCsv() {
        BasicReporter reporter = new CsvBasicReporter(",", System.lineSeparator(), UTF_8);
        Report actual = reporter.generate(TestDataProvider.getReportDefinition());
        Try.of(() -> IOUtils.resourceToByteArray("input.csv", getClass().getClassLoader()))
                .map(a -> Report.of("report", a, CSV, UTF_8))
                .peek(r -> assertEquals(r, actual))
                .orElseRun(t -> fail());
    }

    @Test
    void successfullyGeneratesTsv() {
        BasicReporter reporter = new CsvBasicReporter("\t", System.lineSeparator(), UTF_8);
        Report actual = reporter.generate(TestDataProvider.getReportDefinition());
        Try.of(() -> IOUtils.resourceToByteArray("input.tsv", getClass().getClassLoader()))
                .map(a -> Report.of("report", a, CSV, UTF_8))
                .peek(r -> assertEquals(r, actual))
                .orElseRun(t -> fail());
    }

    @Test
    void tryGenerateReturnsRightOnSuccess() {
        BasicReporter reporter = new CsvBasicReporter(",", System.lineSeparator(), UTF_8);
        Either<ReportingException, Report> result = reporter.tryGenerate(TestDataProvider.getReportDefinition());
        assertTrue(result.isRight());
        assertEquals(CSV, result.get().getType());
        assertEquals("report", result.get().getName());
    }

    @Test
    void tryGenerateReturnsLeftForMultipleSheets() {
        BasicReporter reporter = new CsvBasicReporter(",", System.lineSeparator(), UTF_8);
        ReportDefinition multiSheet = TestDataProvider.getMultiSheetReportDefinition();
        Either<ReportingException, Report> result = reporter.tryGenerate(multiSheet);
        assertTrue(result.isLeft());
    }

    @Test
    void generateThrowsReportingExceptionForMultipleSheets() {
        BasicReporter reporter = new CsvBasicReporter(",", System.lineSeparator(), UTF_8);
        ReportDefinition multiSheet = TestDataProvider.getMultiSheetReportDefinition();
        try {
            reporter.generate(multiSheet);
            fail("Expected ReportingException");
        } catch (ReportingException e) {
            assertTrue(e.getMessage().contains("multiple sheets"));
        }
    }

    @Test
    void generatedReportHasCorrectFullName() {
        BasicReporter reporter = new CsvBasicReporter(",", System.lineSeparator(), UTF_8);
        Report report = reporter.generate(TestDataProvider.getReportDefinition());
        assertEquals("report.csv", report.getFullName());
    }
}
