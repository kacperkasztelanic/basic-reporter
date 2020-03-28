package com.github.kkasztel.basicreporter.service.excel;

import com.github.kkasztel.basicreporter.model.Report;
import com.github.kkasztel.basicreporter.model.ReportDefinition;
import com.github.kkasztel.basicreporter.model.ReportType;

import java.nio.charset.StandardCharsets;

import io.vavr.control.Try;

public class XlsxBasicReporter extends AbstractExcelBasicReporter {

    public XlsxBasicReporter() {
        super(false, false);
    }

    public XlsxBasicReporter(boolean useAutosize, boolean bordered) {
        super(useAutosize, bordered);
    }

    @Override
    public Report generate(ReportDefinition definition) {
        return Try.of(() -> generateData(definition, true))//
                .map(d -> Report.of(//
                        definition.getName(),//
                        d,//
                        ReportType.XLSX,//
                        StandardCharsets.UTF_8//
                        )//
                )//
                .getOrElseThrow(t -> new RuntimeException(t.getMessage(), t));
    }
}
