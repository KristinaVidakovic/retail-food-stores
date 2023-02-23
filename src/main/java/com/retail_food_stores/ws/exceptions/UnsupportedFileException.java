package com.retail_food_stores.ws.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

public class UnsupportedFileException extends AppException {

    public static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String DEFAULT_MESSAGE = "Unsupported file provided! You need to provide CSV file!";

    HttpStatus status = HTTP_STATUS;

    public UnsupportedFileException() {
        this(DEFAULT_MESSAGE);
    }

    UnsupportedFileException(String message) {
        super(HTTP_STATUS, message);
    }

    @Override
    @JsonIgnore
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    @Override
    @JsonIgnore
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    String getErrorCode() {
        return ErrorUtils.getErrorCode(getHttpStatus().value(), "Unsupported file provided! You need to provide CSV file!");
    }
}
