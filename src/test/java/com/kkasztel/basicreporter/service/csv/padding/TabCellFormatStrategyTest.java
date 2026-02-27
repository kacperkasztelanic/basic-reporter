package com.kkasztel.basicreporter.service.csv.padding;

import com.kkasztel.basicreporter.model.ReportDefinition;
import com.kkasztel.basicreporter.service.TestDataProvider;
import io.vavr.collection.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TabCellFormatStrategyTest {

    @ParameterizedTest
    @MethodSource("provideColumn")
    void correctlyFormatsInput(String input, String expected) {
        ReportDefinition reportDefinition = TestDataProvider.getReportDefinition();
        CellFormatStrategy cellFormatStrategy = CellFormatStrategyFactory.createCellFormatStrategy(
                "\t",
                reportDefinition.getSheets().head().getData()
        );
        assertEquals(expected, cellFormatStrategy.format(input, 0));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideColumn() {
        return Stream.of(
                Arguments.of("Col1", "Col1   \t"),
                Arguments.of("Shannon", "Shannon\t"),
                Arguments.of("", "       \t"),
                Arguments.of("Wing", "Wing   \t"),
                Arguments.of("Tate", "Tate   \t"),
                Arguments.of("Clare", "Clare  \t")
        );
    }
}
