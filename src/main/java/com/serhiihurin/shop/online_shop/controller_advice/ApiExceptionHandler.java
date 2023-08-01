package com.serhiihurin.shop.online_shop.controller_advice;

import com.serhiihurin.shop.online_shop.exception.ApiException;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.exception.PurchaseException;
import com.serhiihurin.shop.online_shop.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {
    private final Logger logger;

    @ExceptionHandler(value = ApiRequestException.class)
    public ResponseEntity<ApiException> handleApiRequestException(ApiRequestException exception) {
        ApiException apiException = new ApiException(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        logger.warn("ApiRequestException caught with HTTP status: {} and message: {}",
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
        logger.warn("PurchaseException caught with HTTP status: {} and message: {}",
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
        logger.warn("UnauthorizedAccessException caught with HTTP status: {} and message: {}",
                HttpStatus.FORBIDDEN,
                exception.getMessage());
        return new ResponseEntity<>(apiException, HttpStatus.FORBIDDEN);
    }
}
