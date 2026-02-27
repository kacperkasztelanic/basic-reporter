package com.kkasztel.basicreporter.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.kkasztel.basicreporter.model.ReportDefinition.Sheet;
import com.kkasztel.basicreporter.model.ReportDefinition.Table;

import static io.vavr.API.Vector;
import io.vavr.collection.IndexedSeq;

class ReportDefinitionTest {

    private final Table table = Table.of(
            Vector("A", "B", "C"),
            Vector(
                    Vector("a1", "b1", "c1"),
                    Vector("a2", "b2", "c2")
            )
    );

    // --- Table construction ---

    @Test
    void tableCreationWithValidData() {
        Table t = Table.of(Vector("X", "Y"), Vector(Vector("1", "2"), Vector("3", "4")));
        assertEquals(2, t.getTitleRow().size());
        assertEquals(2, t.getData().size());
    }

    @Test
    void tableCreationWithEmptyData() {
        Table t = Table.of(Vector("X", "Y"), Vector());
        assertEquals(2, t.getTitleRow().size());
        assertEquals(0, t.getData().size());
    }

    @Test
    void tableCreationWithMismatchedColumnsThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                Table.of(Vector("A", "B"), Vector(Vector("1", "2", "extra")))
        );
    }

    @Test
    void tableCreationWithPartialMismatchThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                Table.of(Vector("A", "B"), Vector(Vector("1", "2"), Vector("3")))
        );
    }

    // --- getColumnTitle ---

    @Test
    void getColumnTitleReturnsCorrectValue() {
        assertEquals("A", table.getColumnTitle(0));
        assertEquals("B", table.getColumnTitle(1));
        assertEquals("C", table.getColumnTitle(2));
    }

    @Test
    void getColumnTitleWithNegativeIndexThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.getColumnTitle(-1));
    }

    @Test
    void getColumnTitleWithTooHighIndexThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.getColumnTitle(3));
    }

    // --- getDataRow ---

    @Test
    void getDataRowReturnsCorrectValues() {
        IndexedSeq<String> row = table.getDataRow(0);
        assertEquals(Vector("a1", "b1", "c1"), row);
    }

    @Test
    void getDataRowWithNegativeIndexThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.getDataRow(-1));
    }

    @Test
    void getDataRowWithTooHighIndexThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.getDataRow(2));
    }

    // --- getDataColumn ---

    @Test
    void getDataColumnReturnsCorrectValues() {
        IndexedSeq<String> column = table.getDataColumn(0);
        assertEquals(Vector("a1", "a2"), column);
    }

    @Test
    void getDataColumnWithNegativeIndexThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.getDataColumn(-1));
    }

    @Test
    void getDataColumnWithTooHighIndexThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.getDataColumn(3));
    }

    // --- getDataCell ---

    @Test
    void getDataCellReturnsCorrectValue() {
        assertEquals("b2", table.getDataCell(1, 1));
    }

    @Test
    void getDataCellWithNegativeRowThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.getDataCell(-1, 0));
    }

    @Test
    void getDataCellWithNegativeColumnThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.getDataCell(0, -1));
    }

    @Test
    void getDataCellWithTooHighRowThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.getDataCell(2, 0));
    }

    @Test
    void getDataCellWithTooHighColumnThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.getDataCell(0, 3));
    }

    // --- ReportDefinition factory methods ---

    @Test
    void singleSheetFactoryCreatesOneSheet() {
        ReportDefinition def = ReportDefinition.of("test", table);
        assertEquals("test", def.getName());
        assertEquals(1, def.getSheets().size());
        assertEquals("test", def.getSheets().head().getName());
        assertEquals(table, def.getSheets().head().getData());
    }

    @Test
    void multiSheetFactoryCreatesMultipleSheets() {
        ReportDefinition def = ReportDefinition.of("multi", Vector(
                Sheet.of("Sheet1", table),
                Sheet.of("Sheet2", table)
        ));
        assertEquals("multi", def.getName());
        assertEquals(2, def.getSheets().size());
        assertEquals("Sheet1", def.getSheets().get(0).getName());
        assertEquals("Sheet2", def.getSheets().get(1).getName());
    }

    // --- toString ---

    @Test
    void reportDefinitionToStringContainsName() {
        ReportDefinition def = ReportDefinition.of("myReport", table);
        assertEquals("ReportDefinition(name=myReport)", def.toString());
    }

    @Test
    void sheetToStringContainsName() {
        Sheet sheet = Sheet.of("mySheet", table);
        assertEquals("ReportDefinition.Sheet(name=mySheet)", sheet.toString());
    }

    @Test
    void tableToStringContainsTitleRow() {
        String str = table.toString();
        assertEquals("ReportDefinition.Table(titleRow=Vector(A, B, C))", str);
    }
}
