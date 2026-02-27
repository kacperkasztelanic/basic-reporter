package com.kkasztel.basicreporter.service.common;

import com.kkasztel.basicreporter.model.ReportDefinition.Table;

import io.vavr.Function2;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility for computing the maximum string length of each column in a {@link Table}.
 * <p>
 * The returned function computes the maximum trimmed length across all data cells
 * and the title cell for a given column index. This is useful for formatting
 * fixed-width or padded text output.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ColumnLengthFinder {

    /**
     * Returns a function that computes the maximum trimmed string length for a column.
     * <p>
     * The returned {@link Function2} takes a {@link Table} and a column index,
     * and returns the length of the longest trimmed cell value in that column
     * (including the title row).
     *
     * @return a function {@code (Table, columnIndex) -> maxLength}
     */
    public static Function2<Table, Integer, Integer> getLengthFunction() {
        return (t, i) -> t.getDataColumn(i).append(t.getTitleRow().get(i))
                .map(String::trim)
                .map(String::length)
                .max()
                .getOrElse(0);
    }
}
