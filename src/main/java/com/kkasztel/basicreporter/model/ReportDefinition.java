package com.kkasztel.basicreporter.model;

import io.vavr.collection.IndexedSeq;
import lombok.ToString;
import lombok.Value;

import static io.vavr.API.Vector;

/**
 * Defines the structure and content of a report to be generated.
 * <p>
 * A report definition consists of a name and one or more {@link Sheet}s, each
 * containing a {@link Table} of data. For formats that support multiple sheets
 * (e.g., XLS, XLSX), multiple sheets can be provided. For single-sheet formats
 * (e.g., CSV), only one sheet is allowed.
 * <p>
 * Use the static factory methods to create instances:
 * <ul>
 * <li>{@link #of(String, IndexedSeq)} — multi-sheet definition</li>
 * <li>{@link #of(String, Table)} — single-sheet definition (sheet name defaults
 * to the report name)</li>
 * </ul>
 */
@Value(staticConstructor = "of")
@ToString(of = {"name"})
public class ReportDefinition {

    /**
     * The name of the report.
     */
    String name;
    /**
     * The sheets comprising this report.
     */
    IndexedSeq<Sheet> sheets;

    /**
     * Creates a single-sheet report definition where the sheet name defaults to
     * the report name.
     *
     * @param name the report name
     * @param data the table data for the single sheet
     * @return a new report definition with one sheet
     */
    public static ReportDefinition of(String name, Table data) {
        return new ReportDefinition(name, Vector(Sheet.of(name, data)));
    }

    /**
     * Represents a single sheet within a report, containing a name and tabular
     * data.
     */
    @Value(staticConstructor = "of")
    @ToString(of = {"name"})
    public static class Sheet {

        /**
         * The name of the sheet.
         */
        String name;
        /**
         * The tabular data of the sheet.
         */
        Table data;
    }

    /**
     * Represents a table of data consisting of a title (header) row and zero or
     * more data rows.
     * <p>
     * All rows must have the same number of columns as the title row. This
     * invariant is enforced during construction.
     */
    @Value
    @ToString(of = {"titleRow"})
    public static class Table {

        /**
         * The column headers.
         */
        IndexedSeq<String> titleRow;
        /**
         * The data rows.
         */
        IndexedSeq<IndexedSeq<String>> data;

        /**
         * Creates a new table with the specified title row and data rows.
         *
         * @param titleRow the column headers
         * @param data the data rows; each row must have the same number of
         * columns as {@code titleRow}
         * @return a new table instance
         * @throws IllegalArgumentException if any data row has a different
         * column count than the title row
         */
        public static Table of(IndexedSeq<String> titleRow, IndexedSeq<IndexedSeq<String>> data) {
            return new Table(titleRow, data);
        }

        private Table(IndexedSeq<String> titleRow, IndexedSeq<IndexedSeq<String>> data) {
            validate(titleRow, data);
            this.titleRow = titleRow;
            this.data = data;
        }

        /**
         * Returns the title of the column at the specified index.
         *
         * @param index the zero-based column index
         * @return the column title
         * @throws IllegalArgumentException if the index is out of range
         */
        public String getColumnTitle(int index) {
            validateColumnIndex(index);
            return titleRow.get(index);
        }

        /**
         * Returns the data row at the specified index.
         *
         * @param index the zero-based row index
         * @return the data row as an indexed sequence of strings
         * @throws IllegalArgumentException if the index is out of range
         */
        public IndexedSeq<String> getDataRow(int index) {
            validateRowIndex(index);
            return data.get(index);
        }

        /**
         * Returns all values in the column at the specified index.
         *
         * @param index the zero-based column index
         * @return the column values as an indexed sequence of strings
         * @throws IllegalArgumentException if the index is out of range
         */
        public IndexedSeq<String> getDataColumn(int index) {
            validateColumnIndex(index);
            return data.map(r -> r.get(index));
        }

        /**
         * Returns the cell value at the specified row and column.
         *
         * @param row the zero-based row index
         * @param column the zero-based column index
         * @return the cell value
         * @throws IllegalArgumentException if either index is out of range
         */
        public String getDataCell(int row, int column) {
            validateRowIndex(row);
            validateColumnIndex(column);
            return data.get(row).get(column);
        }

        private void validateRowIndex(int row) {
            if (row < 0 || row >= data.size()) {
                throw new IllegalArgumentException("Row index out of range: " + row + ", valid range: [0, " + (data.size() - 1) + "]");
            }
        }

        private void validateColumnIndex(int column) {
            if (column < 0 || column >= titleRow.size()) {
                throw new IllegalArgumentException("Column index out of range: " + column + ", valid range: [0, " + (titleRow.size() - 1) + "]");
            }
        }

        private static void validate(IndexedSeq<String> titleRow, IndexedSeq<IndexedSeq<String>> data) {
            if (data.exists(r -> r.size() != titleRow.size())) {
                throw new IllegalArgumentException(
                        "Number of columns of each row must equal the number of columns of the title row: " + titleRow.size()
                );
            }
        }
    }
}
