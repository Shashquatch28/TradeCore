package com.tradecore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ===================== TRADER =====================

    @ExceptionHandler(TraderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleTraderNotFound(TraderNotFoundException ex) {
        return buildResponse(404, ex.getMessage());
    }

    // ===================== ORDER =====================

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleOrderNotFound(OrderNotFoundException ex) {
        return buildResponse(404, ex.getMessage());
    }

    @ExceptionHandler(InvalidOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleInvalidOrder(InvalidOrderException ex) {
        return buildResponse(400, ex.getMessage());
    }

    // ===================== PORTFOLIO =====================

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleInsufficientFunds(InsufficientFundsException ex) {
        return buildResponse(400, ex.getMessage());
    }

    @ExceptionHandler(InsufficientHoldingsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleInsufficientHoldings(InsufficientHoldingsException ex) {
        return buildResponse(400, ex.getMessage());
    }

    // ===================== VALIDATION =====================

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(400, ex.getMessage());
    }

    // ===================== FALLBACK =====================

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneric(Exception ex) {
        return buildResponse(500, "Internal Server Error");
    }

    // ===================== HELPER =====================

    private Map<String, Object> buildResponse(int status, String message) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status,
                "error", message
        );
    }
}