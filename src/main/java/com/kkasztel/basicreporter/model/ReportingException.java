package com.kkasztel.basicreporter.model;

/**
 * Unchecked exception indicating an error during report generation.
 * <p>
 * Wraps the underlying cause of the failure and is used as the left value in
 * {@link io.vavr.control.Either} results returned by
 * {@link com.kkasztel.basicreporter.service.BasicReporter#tryGenerate(ReportDefinition)}.
 */
public class ReportingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@code ReportingException} with the specified detail
     * message and cause.
     *
     * @param message the detail message
     * @param cause the underlying cause of the reporting failure
     */
    public ReportingException(String message, Throwable cause) {
        super(message, cause);
    }
}
