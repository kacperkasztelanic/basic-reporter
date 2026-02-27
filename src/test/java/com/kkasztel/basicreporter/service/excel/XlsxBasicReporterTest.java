package com.kkasztel.basicreporter.service.excel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.kkasztel.basicreporter.model.Report;
import com.kkasztel.basicreporter.model.ReportDefinition;
import static com.kkasztel.basicreporter.model.ReportType.XLSX;
import com.kkasztel.basicreporter.model.ReportingException;
import com.kkasztel.basicreporter.service.BasicReporter;
import com.kkasztel.basicreporter.service.TestDataProvider;

import io.vavr.control.Either;

class XlsxBasicReporterTest {

    @Test
    void successfullyGeneratesXlsxWithAutosizeAndBorders() {
        BasicReporter reporter = new XlsxBasicReporter(true, true);
        ReportDefinition report = TestDataProvider.getMultiSheetReportDefinition();
        Report actual = reporter.generate(report);
        assertEquals(report.getName(), actual.getName());
        assertEquals(XLSX, actual.getType());
    }

    @Test
    void successfullyGeneratesXlsxWithDefaults() {
        BasicReporter reporter = new XlsxBasicReporter();
        Report actual = reporter.generate(TestDataProvider.getReportDefinition());
        assertEquals("report", actual.getName());
        assertEquals(XLSX, actual.getType());
        assertTrue(actual.getBytes().length > 0);
    }

    @Test
    void successfullyGeneratesXlsxWithAutosizeOnly() {
        BasicReporter reporter = new XlsxBasicReporter(true, false);
        Report actual = reporter.generate(TestDataProvider.getReportDefinition());
        assertEquals(XLSX, actual.getType());
        assertTrue(actual.getBytes().length > 0);
    }

    @Test
    void successfullyGeneratesXlsxWithBordersOnly() {
        BasicReporter reporter = new XlsxBasicReporter(false, true);
        Report actual = reporter.generate(TestDataProvider.getReportDefinition());
        assertEquals(XLSX, actual.getType());
        assertTrue(actual.getBytes().length > 0);
    }

    @Test
    void successfullyGeneratesSingleSheetXlsx() {
        BasicReporter reporter = new XlsxBasicReporter(true, true);
        Report actual = reporter.generate(TestDataProvider.getReportDefinition());
        assertEquals("report", actual.getName());
        assertEquals(XLSX, actual.getType());
    }

    @Test
    void tryGenerateReturnsRight() {
        BasicReporter reporter = new XlsxBasicReporter();
        Either<ReportingException, Report> result = reporter.tryGenerate(TestDataProvider.getReportDefinition());
        assertTrue(result.isRight());
        assertEquals(XLSX, result.get().getType());
    }

    @Test
    void generatedReportHasCorrectFullName() {
        BasicReporter reporter = new XlsxBasicReporter();
        Report report = reporter.generate(TestDataProvider.getReportDefinition());
        assertEquals("report.xlsx", report.getFullName());
    }
}
