package com.kkasztel.basicreporter.service.excel;

import com.kkasztel.basicreporter.model.Report;
import com.kkasztel.basicreporter.model.ReportDefinition;
import com.kkasztel.basicreporter.model.ReportingException;
import com.kkasztel.basicreporter.service.BasicReporter;
import com.kkasztel.basicreporter.service.TestDataProvider;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import static com.kkasztel.basicreporter.model.ReportType.XLS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XlsBasicReporterTest {

    @Test
    void successfullyGeneratesXlsWithAutosizeAndBorders() {
        BasicReporter reporter = new XlsBasicReporter(true, true);
        ReportDefinition report = TestDataProvider.getMultiSheetReportDefinition();
        Report actual = reporter.generate(report);
        assertEquals(report.getName(), actual.getName());
        assertEquals(XLS, actual.getType());
    }

    @Test
    void successfullyGeneratesXlsWithDefaults() {
        BasicReporter reporter = new XlsBasicReporter();
        Report actual = reporter.generate(TestDataProvider.getReportDefinition());
        assertEquals("report", actual.getName());
        assertEquals(XLS, actual.getType());
        assertTrue(actual.getBytes().length > 0);
    }

    @Test
    void successfullyGeneratesXlsWithAutosizeOnly() {
        BasicReporter reporter = new XlsBasicReporter(true, false);
        Report actual = reporter.generate(TestDataProvider.getReportDefinition());
        assertEquals(XLS, actual.getType());
        assertTrue(actual.getBytes().length > 0);
    }

    @Test
    void successfullyGeneratesXlsWithBordersOnly() {
        BasicReporter reporter = new XlsBasicReporter(false, true);
        Report actual = reporter.generate(TestDataProvider.getReportDefinition());
        assertEquals(XLS, actual.getType());
        assertTrue(actual.getBytes().length > 0);
    }

    @Test
    void successfullyGeneratesSingleSheetXls() {
        BasicReporter reporter = new XlsBasicReporter(true, true);
        Report actual = reporter.generate(TestDataProvider.getReportDefinition());
        assertEquals("report", actual.getName());
        assertEquals(XLS, actual.getType());
    }

    @Test
    void tryGenerateReturnsRight() {
        BasicReporter reporter = new XlsBasicReporter();
        Either<ReportingException, Report> result = reporter.tryGenerate(TestDataProvider.getReportDefinition());
        assertTrue(result.isRight());
        assertEquals(XLS, result.get().getType());
    }

    @Test
    void generatedReportHasCorrectFullName() {
        BasicReporter reporter = new XlsBasicReporter();
        Report report = reporter.generate(TestDataProvider.getReportDefinition());
        assertEquals("report.xls", report.getFullName());
    }
}
