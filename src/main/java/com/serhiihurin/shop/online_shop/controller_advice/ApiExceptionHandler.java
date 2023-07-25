package com.serhiihurin.shop.online_shop.controller_advice;

import com.serhiihurin.shop.online_shop.exception.ApiException;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException exception) {
        ApiException apiException = new ApiException(
                exception.getMessage(),
                exception,
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))

        );

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}
