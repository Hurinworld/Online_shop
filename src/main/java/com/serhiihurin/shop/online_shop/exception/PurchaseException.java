package com.serhiihurin.shop.online_shop.exception;

import org.springframework.http.HttpStatus;

//TODO add constant field for status code to all exceptions //done
//TODO create a superclass for custom exceptions //done
public class PurchaseException extends ApiException {
    public final HttpStatus httpStatus = HttpStatus.CONFLICT;
    public PurchaseException(String message) {
        super(message);
        super.httpStatus = httpStatus;
    }

    public PurchaseException(String message, Throwable cause) {
        super(message, cause);
        super.httpStatus = httpStatus;
    }
}
