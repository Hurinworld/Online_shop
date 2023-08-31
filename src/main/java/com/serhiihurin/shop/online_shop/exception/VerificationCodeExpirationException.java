package com.serhiihurin.shop.online_shop.exception;

import org.springframework.http.HttpStatus;

public class VerificationCodeExpirationException extends ApiException{
    public final HttpStatus httpStatus = HttpStatus.FORBIDDEN;

    public VerificationCodeExpirationException(String message) {
        super(message);
        super.httpStatus = httpStatus;
    }

    public VerificationCodeExpirationException(String message, Throwable cause) {
        super(message, cause);
        super.httpStatus = httpStatus;
    }
}
