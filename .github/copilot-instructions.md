# Copilot Instructions

## Build, Test, and Lint

```bash
# Build
./gradlew build

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.kkasztel.basicreporter.service.csv.CsvBasicReporterTest"

# Run a single test method
./gradlew test --tests "com.kkasztel.basicreporter.service.csv.CsvBasicReporterTest.someMethodName"

# Generate JaCoCo coverage report
./gradlew jacocoTestReport
```

## Architecture

This is a Java 8 library (`com.kkasztel.basicreporter`) for generating tabular reports in CSV/TSV/XLS/XLSX formats. It is published to GitHub Packages and JitPack.

**Package layout:**
- `model/` — Immutable value objects: `ReportDefinition`, `ReportDefinition.Sheet`, `ReportDefinition.Table`, `Report`, `ReportType`, `ReportingException`
- `service/BasicReporter.java` — Core interface with `tryGenerate` and `generate`
- `service/csv/` — `CsvBasicReporter` plus a `padding/` sub-package for TSV column alignment strategies
- `service/excel/` — `AbstractExcelBasicReporter` (shared POI logic), extended by `XlsBasicReporter` and `XlsxBasicReporter`
- `service/common/` — `ColumnLengthFinder` utility used by Excel reporters for column width calculation

**Data flow:** caller builds a `ReportDefinition` (name + one or more `Sheet`s each with a `Table`) → passes it to a `BasicReporter` implementation → receives a `Report` (name, byte content, `ReportType`, charset).

## Key Conventions

**Vavr everywhere:** Collections use Vavr's `IndexedSeq` / `Vector` (imported via `io.vavr.API.Vector`), not `java.util` collections. Functional types (`Either`, `Try`, `Function1`) are from Vavr, not Java stdlib.

**Immutable model with Lombok `@Value`:** All model classes are annotated `@Value(staticConstructor = "of")`. Always use the static `of(...)` factory method — constructors are private. Never add mutable fields to model classes.

**Dual error-handling API:** `BasicReporter` always implements `tryGenerate` returning `Either<ReportingException, Report>`. The `generate` default method unwraps it and throws. New reporter implementations must override `tryGenerate`; `generate` is inherited for free.

**Excel reporters share an abstract base:** `AbstractExcelBasicReporter` handles all POI workbook/sheet/cell/style creation. Concrete subclasses (`XlsBasicReporter`, `XlsxBasicReporter`) only supply the `ReportType` and delegate to `generateData(definition, xlsx)`. Keep shared Excel logic in the abstract class.

**TSV padding via strategy pattern:** `CellFormatStrategyFactory` inspects the separator and returns either a passthrough or a `TabCellFormatStrategy` that space-pads values to the widest column. When adding new separator-specific formatting, add a new `CellFormatStrategy` implementation and register it in the factory.

**CsvBasicReporter is single-sheet only:** Passing a multi-sheet `ReportDefinition` throws `IllegalArgumentException` inside `tryGenerate`, which is mapped to `ReportingException` on the left side of the `Either`.

**Test data:** Shared fixture data lives in `src/test/java/.../service/TestDataProvider.java`. Add new shared test data there rather than duplicating it across test classes.

**Column width units for POI:** When not using `autoSizeColumn`, the default width formula is `(maxColumnLength + 5) * 256` (POI uses 1/256th of a character width).
