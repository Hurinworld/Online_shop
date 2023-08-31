package com.serhiihurin.shop.online_shop.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedAccessException extends ApiException {
    public final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
    public UnauthorizedAccessException(String message) {
        super(message);
        super.httpStatus = httpStatus;
    }

    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
        super.httpStatus = httpStatus;
    }
}
