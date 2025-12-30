package com.file_service.shared.infrastructure.logging;

import com.file_service.shared.application.ValidationException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        String traceId = MDC.get("traceId");

        log.warn("Validation error - TraceId: {} - Error: {}", traceId, e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Validation Error")
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .traceId(traceId)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        String traceId = MDC.get("traceId");

        log.warn("Invalid argument - TraceId: {} - Error: {}", traceId, e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Invalid Argument")
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .traceId(traceId)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        String traceId = MDC.get("traceId");

        log.warn("Invalid state - TraceId: {} - Error: {}", traceId, e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Invalid State")
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .traceId(traceId)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String traceId = MDC.get("traceId");

        String errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Bean validation error - TraceId: {} - Errors: {}", traceId, errors);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Validation Error")
                .message("Invalid request parameters: " + errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .traceId(traceId)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        String traceId = MDC.get("traceId");

        log.warn("Malformed request body - TraceId: {} - Error: {}", traceId, e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Malformed Request")
                .message("Invalid JSON format in request body")
                .status(HttpStatus.BAD_REQUEST.value())
                .traceId(traceId)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        String traceId = MDC.get("traceId");

        log.warn("Type mismatch - TraceId: {} - Parameter: {} - Error: {}",
                traceId, e.getName(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Type Mismatch")
                .message(String.format("Invalid value for parameter '%s'", e.getName()))
                .status(HttpStatus.BAD_REQUEST.value())
                .traceId(traceId)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        String traceId = MDC.get("traceId");

        log.error("Runtime exception - TraceId: {} - Error: {}", traceId, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Internal Error")
                .message("An error occurred while processing your request")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .traceId(traceId)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        String traceId = MDC.get("traceId");

        log.error("Unexpected exception - TraceId: {} - Error: {}", traceId, e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .traceId(traceId)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @Getter
    public static class ErrorResponse {
        private String error;
        private String message;
        private Integer status;
        private String traceId;
        private Instant timestamp;

        private ErrorResponse() {}

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private final ErrorResponse response = new ErrorResponse();

            public Builder error(String error) {
                response.error = error;
                return this;
            }

            public Builder message(String message) {
                response.message = message;
                return this;
            }

            public Builder status(Integer status) {
                response.status = status;
                return this;
            }

            public Builder traceId(String traceId) {
                response.traceId = traceId;
                return this;
            }

            public Builder timestamp(Instant timestamp) {
                response.timestamp = timestamp;
                return this;
            }

            public ErrorResponse build() {
                return response;
            }
        }
    }
}