package com.kkasztel.basicreporter.service.excel;

import com.kkasztel.basicreporter.model.Report;
import com.kkasztel.basicreporter.model.ReportDefinition;
import com.kkasztel.basicreporter.model.ReportDefinition.Table;
import com.kkasztel.basicreporter.model.ReportingException;
import com.kkasztel.basicreporter.service.BasicReporter;
import com.kkasztel.basicreporter.service.common.ColumnLengthFinder;
import io.vavr.Function1;
import io.vavr.collection.Iterator;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.IntUnaryOperator;

abstract class AbstractExcelBasicReporter implements BasicReporter {

    private static final BorderStyle BORDER_STYLE = BorderStyle.THIN;
    private static final IntUnaryOperator DEFAULT_WIDTH = i -> (i + 5) * 256;

    private final boolean useAutosize;
    private final boolean bordered;

    protected AbstractExcelBasicReporter(boolean useAutosize, boolean bordered) {
        this.useAutosize = useAutosize;
        this.bordered = bordered;
    }

    @Override
    public Either<ReportingException, Report> tryGenerate(ReportDefinition definition) {
        return generateReport(definition)
                .toEither()
                .mapLeft(t -> new ReportingException(t.getMessage(), t));
    }

    protected abstract Try<Report> generateReport(ReportDefinition definition);

    protected byte[] generateData(ReportDefinition definition, boolean xlsx) throws IOException {
        try (Workbook workbook = xlsx ? new XSSFWorkbook() : new HSSFWorkbook()) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            CellStyle style = createStyle(workbook);
            definition.getSheets().forEach(s -> createSheet(workbook, s, style));
            workbook.write(os);
            return os.toByteArray();
        }
    }

    private void createSheet(Workbook workbook, ReportDefinition.Sheet sheetDefinition, CellStyle style) {
        Sheet sheet = workbook.createSheet(sheetDefinition.getName());
        createTitleRow(sheet, sheetDefinition, style);
        createDataRows(sheet, sheetDefinition, style);
        resize(sheet, sheetDefinition.getData());
    }

    private void createTitleRow(Sheet sheet, ReportDefinition.Sheet sheetDefinition, CellStyle style) {
        Row row = sheet.createRow(0);
        sheetDefinition.getData().getTitleRow()
                .zipWithIndex()
                .forEach(p -> createCell(row, p._2, p._1, style));
    }

    private void createDataRows(Sheet sheet, ReportDefinition.Sheet sheetDefinition, CellStyle style) {
        sheetDefinition.getData().getData()
                .zip(Iterator.from(1))
                .forEach(r -> {
                    Row row = sheet.createRow(r._2);
                    r._1.zipWithIndex()
                            .forEach(c -> createCell(row, c._2, c._1, style));
                });
    }

    private void createCell(Row row, int index, String value, CellStyle style) {
        Cell cell = row.createCell(index);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void resize(Sheet sheet, Table data) {
        Function1<Integer, Integer> columnLengthFunction = ColumnLengthFinder.getLengthFunction()
                .curried()
                .apply(data)
                .memoized();
        Iterator.range(0, data.getTitleRow().size())
                .forEach(i -> resize(sheet, columnLengthFunction, i));
    }

    private void resize(Sheet sheet, Function1<Integer, Integer> columnLengthFunction, int columnIndex) {
        if (useAutosize) {
            sheet.autoSizeColumn(columnIndex);
        } else {
            sheet.setColumnWidth(columnIndex, DEFAULT_WIDTH.applyAsInt(columnLengthFunction.apply(columnIndex)));
        }
    }

    private CellStyle createStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        if (bordered) {
            setBorders(style);
        }
        return style;
    }

    private void setBorders(CellStyle style) {
        style.setBorderTop(BORDER_STYLE);
        style.setBorderBottom(BORDER_STYLE);
        style.setBorderLeft(BORDER_STYLE);
        style.setBorderRight(BORDER_STYLE);
    }
}
