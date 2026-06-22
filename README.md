[![](https://jitpack.io/v/kacperkasztelanic/basic-reporter.svg)](https://jitpack.io/#kacperkasztelanic/basic-reporter)
[![](https://jitci.com/gh/kacperkasztelanic/basic-reporter/svg)](https://jitci.com/gh/kacperkasztelanic/basic-reporter)

# Basic Reporter

A lightweight Java 8+ library for generating simple tabular reports in multiple formats.

## Supported Formats

| Format | Class | Multi-sheet | Extension |
|--------|-------|:-----------:|-----------|
| CSV | `CsvBasicReporter` | No | `.csv` |
| TSV (tab-separated, padded) | `CsvBasicReporter` | No | `.csv` |
| XLS (Excel 97-2003) | `XlsBasicReporter` | Yes | `.xls` |
| XLSX (Excel 2007+) | `XlsxBasicReporter` | Yes | `.xlsx` |

## Features

- **Functional error handling** — every reporter exposes both `tryGenerate` (returns `Either<ReportingException, Report>`) and `generate` (throws on failure)
- **Immutable data model** — `ReportDefinition`, `Sheet`, `Table`, and `Report` are all value objects
- **Multi-sheet support** — XLS and XLSX reporters can produce workbooks with multiple sheets
- **Column auto-sizing** — Excel reporters support optional auto-sizing of column widths
- **Cell borders** — Excel reporters support optional thin borders on all cells
- **TSV alignment** — when using a tab separator, cell values are automatically padded with spaces for visual alignment
- **Built on Vavr** — uses Vavr collections and functional types throughout the API

## Installation

### Gradle (JitPack)

Add JitPack to your repositories and the dependency:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.kacperkasztelanic:basic-reporter:1.2.0'
}
```

### Maven (JitPack)

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.kacperkasztelanic</groupId>
    <artifactId>basic-reporter</artifactId>
    <version>1.2.0</version>
</dependency>
```

## Usage

### Define report data

```java
import com.kkasztel.basicreporter.model.ReportDefinition;
import com.kkasztel.basicreporter.model.ReportDefinition.Table;
import static io.vavr.API.Vector;

// Single-sheet definition
Table table = Table.of(
    Vector("Name", "Email", "Code"),
    Vector(
        Vector("Alice", "alice@example.com", "A01"),
        Vector("Bob", "bob@example.com", "B02")
    )
);
ReportDefinition definition = ReportDefinition.of("users", table);
```

### Generate a CSV report

```java
import com.kkasztel.basicreporter.service.csv.CsvBasicReporter;
import java.nio.charset.StandardCharsets;

BasicReporter reporter = new CsvBasicReporter(",", System.lineSeparator(), StandardCharsets.UTF_8);
Report report = reporter.generate(definition);

// report.getBytes()    → the raw CSV content
// report.getFullName() → "users.csv"
```

### Generate a TSV report (tab-separated, padded)

```java
BasicReporter reporter = new CsvBasicReporter("\t", System.lineSeparator(), StandardCharsets.UTF_8);
Report report = reporter.generate(definition);
```

### Generate an Excel report (XLSX)

```java
import com.kkasztel.basicreporter.service.excel.XlsxBasicReporter;

// With auto-sized columns and cell borders
BasicReporter reporter = new XlsxBasicReporter(true, true);
Report report = reporter.generate(definition);

// report.getBytes()    → the raw .xlsx content
// report.getFullName() → "users.xlsx"
```

### Generate an Excel report (XLS)

```java
import com.kkasztel.basicreporter.service.excel.XlsBasicReporter;

// Default settings (no auto-size, no borders)
BasicReporter reporter = new XlsBasicReporter();
Report report = reporter.generate(definition);
```

### Multi-sheet Excel reports

```java
import com.kkasztel.basicreporter.model.ReportDefinition.Sheet;

ReportDefinition multiSheet = ReportDefinition.of("workbook", Vector(
    Sheet.of("Q1", q1Table),
    Sheet.of("Q2", q2Table)
));
Report report = new XlsxBasicReporter(true, true).generate(multiSheet);
```

### Functional error handling

```java
import io.vavr.control.Either;
import com.kkasztel.basicreporter.model.ReportingException;

Either<ReportingException, Report> result = reporter.tryGenerate(definition);

result.peek(report -> System.out.println("Generated: " + report.getFullName()))
      .peekLeft(error -> System.err.println("Failed: " + error.getMessage()));
```

## API Overview

### Model

| Class | Description |
|-------|-------------|
| `ReportDefinition` | Top-level definition: a name and one or more `Sheet`s |
| `ReportDefinition.Sheet` | A named sheet containing a `Table` |
| `ReportDefinition.Table` | Column headers (title row) and data rows; validates column count consistency |
| `Report` | Generated report: name, byte content, type, and charset |
| `ReportType` | Enum of supported formats: `CSV`, `XLS`, `XLSX` |
| `ReportingException` | Unchecked exception wrapping report generation failures |

### Service

| Class | Description |
|-------|-------------|
| `BasicReporter` | Core interface with `tryGenerate` and `generate` methods |
| `CsvBasicReporter` | CSV/TSV reporter with configurable separator, line separator, and charset |
| `XlsBasicReporter` | Excel 97-2003 (.xls) reporter with optional auto-sizing and borders |
| `XlsxBasicReporter` | Excel 2007+ (.xlsx) reporter with optional auto-sizing and borders |

## Dependencies

- [Vavr](https://www.vavr.io/) — functional library for Java
- [Apache POI](https://poi.apache.org/) — Excel file generation
- [Lombok](https://projectlombok.org/) — compile-time boilerplate reduction

## License

[MIT](LICENSE)
