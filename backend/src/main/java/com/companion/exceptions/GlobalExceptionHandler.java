package com.companion.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI(), Map.of());
    }

    @ExceptionHandler({DuplicateResourceException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiErrorDTO> handleConflict(Exception exception, HttpServletRequest request) {
        String message = exception instanceof DuplicateResourceException
                ? exception.getMessage()
                : "E-mail ou matrícula já cadastrados";
        return buildError(HttpStatus.CONFLICT, message, request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage())
        );
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Existem campos inválidos na requisição",
                request.getRequestURI(),
                fieldErrors
        );
    }

    private ResponseEntity<ApiErrorDTO> buildError(
            HttpStatus status,
            String message,
            String path,
            Map<String, String> fieldErrors
    ) {
        ApiErrorDTO error = new ApiErrorDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                fieldErrors
        );
        return ResponseEntity.status(status).body(error);
    }
}
