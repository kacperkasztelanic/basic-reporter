package com.kkasztel.basicreporter.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportTypeTest {

    @ParameterizedTest
    @EnumSource(ReportType.class)
    void getExtensionReturnsLowercaseName(ReportType type) {
        assertEquals(type.name().toLowerCase(), type.getExtension());
    }

    @Test
    void csvExtension() {
        assertEquals("csv", ReportType.CSV.getExtension());
    }

    @Test
    void xlsExtension() {
        assertEquals("xls", ReportType.XLS.getExtension());
    }

    @Test
    void xlsxExtension() {
        assertEquals("xlsx", ReportType.XLSX.getExtension());
    }
}
