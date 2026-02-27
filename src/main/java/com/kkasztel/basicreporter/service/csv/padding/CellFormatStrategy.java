package com.kkasztel.basicreporter.service.csv.padding;

/**
 * Strategy for formatting cell values in text-based report output.
 * <p>
 * Implementations may apply padding, trimming, or other transformations
 * to cell values based on the column index.
 */
@FunctionalInterface
public interface CellFormatStrategy {

    /**
     * Formats a cell value for output.
     *
     * @param input       the raw cell value
     * @param columnIndex the zero-based column index
     * @return the formatted cell value
     */
    String format(String input, int columnIndex);
}
