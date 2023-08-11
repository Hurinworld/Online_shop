package com.serhiihurin.shop.online_shop.controller_advice;

import com.serhiihurin.shop.online_shop.exception.ApiException;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.exception.PurchaseException;
import com.serhiihurin.shop.online_shop.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiExceptionHandler {
    @ExceptionHandler(value = ApiRequestException.class)
    public ResponseEntity<ApiException> handleApiRequestException(ApiRequestException exception) {
        ApiException apiException = new ApiException(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.warn("ApiRequestException caught with HTTP status: {} and message: {}",
                HttpStatus.BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = PurchaseException.class)
    public ResponseEntity<ApiException> handlePurchaseException(PurchaseException exception) {
        ApiException apiException = new ApiException(
                exception.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.warn("PurchaseException caught with HTTP status: {} and message: {}",
                HttpStatus.CONFLICT,
                exception.getMessage());
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = UnauthorizedAccessException.class)
    public ResponseEntity<ApiException> handleUnauthorizedAccessException(UnauthorizedAccessException exception) {
        ApiException apiException = new ApiException(
                exception.getMessage(),
                HttpStatus.FORBIDDEN,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.warn("UnauthorizedAccessException caught with HTTP status: {} and message: {}",
                HttpStatus.FORBIDDEN,
                exception.getMessage());
        return new ResponseEntity<>(apiException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiException> handleException(Exception exception) {
        ApiException apiException = new ApiException(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.warn("Exception caught with HTTP status: {} and message: {}",
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage());
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
