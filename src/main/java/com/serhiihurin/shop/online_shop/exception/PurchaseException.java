package com.serhiihurin.shop.online_shop.exception;

//TODO add constant field for status code to all exceptions
//TODO create an superclass for custom exceptions
public class PurchaseException extends RuntimeException {
    public PurchaseException(String message) {
        super(message);
    }
}
