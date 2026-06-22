# AGENTS.md

This file provides guidance to coding agents when working with code in this repository.

## Overview

`basic-reporter` (`com.kkasztel.basicreporter`) is a lightweight **Java 8** library for generating
simple tabular reports as CSV, TSV, XLS, and XLSX. It is published to GitHub Packages and JitPack.

## Build, Test, Lint

```bash
./gradlew build                  # compile + test + assemble
./gradlew test                   # run all tests
./gradlew jacocoTestReport       # coverage report (build/reports/jacoco)

# Single test class / method
./gradlew test --tests "com.kkasztel.basicreporter.service.csv.CsvBasicReporterTest"
./gradlew test --tests "com.kkasztel.basicreporter.service.csv.CsvBasicReporterTest.someMethodName"
```

Note: source/target is **Java 8** — do not use APIs or syntax newer than Java 8. Lombok and
JaCoCo are wired in via Gradle plugins (no manual annotation processing setup needed).

## Git & Release Workflow

- **Commit subject**: imperative mood (e.g. "Add", "Fix", "Migrate" — not "Added"/"Fixes").
- **Commit body**: leave empty. The subject line is the entire message.
- **Merging**: integrate `dev` into `master` with fast-forward merges only (no merge commits).
- **Pushing**: the user pushes manually. Agents must not push to any remote.

## Architecture

**Data flow:** caller builds a `ReportDefinition` (a name + one or more `Sheet`s, each holding a
`Table`) → passes it to a `BasicReporter` implementation → receives a `Report` (name, byte content,
`ReportType`, charset).

**Package layout:**
- `model/` — immutable value objects: `ReportDefinition` (+ nested `Sheet`, `Table`), `Report`, `ReportType`, `ReportingException`
- `service/BasicReporter.java` — core interface (`tryGenerate`, `generate`)
- `service/csv/` — `CsvBasicReporter`; `padding/` sub-package holds TSV column-alignment strategies
- `service/excel/` — `AbstractExcelBasicReporter` (shared POI logic) extended by `XlsBasicReporter` and `XlsxBasicReporter`
- `service/common/` — `ColumnLengthFinder`, used by Excel reporters for column width

## Key Conventions

- **Vavr everywhere.** Collections are Vavr `IndexedSeq` / `Vector` (`io.vavr.API.Vector`), not
  `java.util`. Functional types (`Either`, `Try`, `Function1`) come from Vavr, not the Java stdlib.

- **Immutable model via Lombok `@Value(staticConstructor = "of")`.** Construct model objects only
  through the static `of(...)` factory; constructors are private. Never add mutable fields.

- **Dual error-handling API.** New reporter implementations override `tryGenerate` returning
  `Either<ReportingException, Report>`. The `generate` default method unwraps and throws, so it is
  inherited for free.

- **Excel reporters share `AbstractExcelBasicReporter`.** It owns all POI workbook/sheet/cell/style
  creation. Concrete subclasses only supply the `ReportType` and delegate to
  `generateData(definition, xlsx)`. Keep shared Excel logic in the abstract class.

- **TSV padding via strategy pattern.** `CellFormatStrategyFactory` inspects the separator and
  returns a passthrough or a `TabCellFormatStrategy` that space-pads to the widest column. Add a new
  `CellFormatStrategy` and register it in the factory for new separator-specific formatting.

- **`CsvBasicReporter` is single-sheet only.** A multi-sheet `ReportDefinition` causes an
  `IllegalArgumentException` inside `tryGenerate`, mapped to `ReportingException` on the `Either` left.

- **POI column width.** Without `autoSizeColumn`, the default width formula is
  `(maxColumnLength + 5) * 256` (POI uses 1/256th of a character width).

- **Shared test fixtures** live in `src/test/java/.../service/TestDataProvider.java` (and
  `src/test/resources/input.csv` / `input.tsv`). Add shared test data there rather than duplicating.
