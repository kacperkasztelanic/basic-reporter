package com.kkasztel.basicreporter.service.csv.padding;

import com.kkasztel.basicreporter.model.ReportDefinition.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.is;

/**
 * Factory for creating {@link CellFormatStrategy} instances based on the field separator.
 * <p>
 * When the separator is a tab character ({@code \t}), a {@link TabCellFormatStrategy}
 * is used to pad cell values with spaces for visual alignment. For all other separators,
 * cell values are passed through unchanged.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CellFormatStrategyFactory {

    /**
     * Creates a {@link CellFormatStrategy} appropriate for the given separator and table data.
     *
     * @param separator the field separator used in the output
     * @param table     the table data (used to compute column widths for padding strategies)
     * @return a cell format strategy suitable for the given separator
     */
    public static CellFormatStrategy createCellFormatStrategy(String separator, Table table) {
        CellFormatStrategy simpleStrategy = (s, i) -> s;
        return Match(separator).of(
                Case($(is("\t")), new TabCellFormatStrategy(table)),
                Case($(), simpleStrategy)
        );
    }
}
