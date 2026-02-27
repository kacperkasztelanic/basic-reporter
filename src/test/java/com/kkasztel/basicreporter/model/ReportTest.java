package com.kkasztel.basicreporter.model;

import static java.nio.charset.StandardCharsets.UTF_8;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ReportTest {

    @Test
    void getFullNameCombinesNameAndExtension() {
        Report report = Report.of("sales", new byte[0], ReportType.CSV, UTF_8);
        assertEquals("sales.csv", report.getFullName());
    }

    @Test
    void getFullNameForXls() {
        Report report = Report.of("data", new byte[0], ReportType.XLS, UTF_8);
        assertEquals("data.xls", report.getFullName());
    }

    @Test
    void getFullNameForXlsx() {
        Report report = Report.of("data", new byte[0], ReportType.XLSX, UTF_8);
        assertEquals("data.xlsx", report.getFullName());
    }

    @Test
    void toStringContainsFullNameAndCharset() {
        Report report = Report.of("test", new byte[0], ReportType.CSV, UTF_8);
        assertEquals("Report(test.csv, UTF-8)", report.toString());
    }

    @Test
    void reportPreservesBytes() {
        byte[] content = "hello".getBytes(UTF_8);
        Report report = Report.of("test", content, ReportType.CSV, UTF_8);
        assertArrayEquals(content, report.getBytes());
    }

    @Test
    void reportPreservesMetadata() {
        Report report = Report.of("myReport", new byte[0], ReportType.XLSX, UTF_8);
        assertEquals("myReport", report.getName());
        assertEquals(ReportType.XLSX, report.getType());
        assertEquals(UTF_8, report.getCharset());
    }
}
