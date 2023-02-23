package com.retail_food_stores.ws.exceptions;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {

    private final HttpStatus httpStatus;

    AppException() {
        super();
        httpStatus = HttpStatus.BAD_REQUEST;
    }

    AppException(String message) {
        super(message);
        httpStatus = HttpStatus.BAD_REQUEST;
    }

    AppException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    AppException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    HttpStatus getHttpStatus() {
        return httpStatus;
    }

    String getErrorCode() {
        return ErrorUtils.getErrorCode(httpStatus.value());
    }
}
