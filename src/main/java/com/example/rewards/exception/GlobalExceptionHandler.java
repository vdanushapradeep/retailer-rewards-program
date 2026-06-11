package com.example.rewards.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;

/**
 * Global exception handler to convert exceptions into a standardized
 * {@link ErrorResponse} payload for API clients.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        /**
         * Handle {@link ResourceNotFoundException} and return a 404 payload.
         */
        public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
                ErrorResponse body = ErrorResponse.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.NOT_FOUND.value())
                                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .message(ex.getMessage())
                                .path(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body);
        }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        /**
         * Handle unsupported HTTP methods and return a 405 payload.
         */
        public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
                        HttpServletRequest req) {
                ErrorResponse body = ErrorResponse.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                                .error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                                .message(ex.getMessage())
                                .path(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body);
        }

        @ExceptionHandler(Exception.class)
        /**
         * Fallback handler for generic exceptions returning a 500 payload.
         */
        public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
                ErrorResponse body = ErrorResponse.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                                .message(ex.getMessage())
                                .path(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        /**
         * Handle {@link MethodArgumentNotValidException} and return a 400 with
         * the validation details.
         */
        public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                        HttpServletRequest req) {
                String details = ex.getBindingResult().getFieldErrors().stream()
                                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                                .collect(Collectors.joining(", "));
                ErrorResponse body = ErrorResponse.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .message(details)
                                .path(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        /**
         * Handle {@link ConstraintViolationException} and return a 400 with
         * violation messages.
         */
        public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                        HttpServletRequest req) {
                String details = ex.getConstraintViolations().stream()
                                .map(cv -> cv.getPropertyPath() + " " + cv.getMessage())
                                .collect(Collectors.joining(", "));
                ErrorResponse body = ErrorResponse.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .message(details)
                                .path(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        /**
         * Handle type mismatch exceptions (e.g., invalid path variable types)
         * and return a 400 payload describing the invalid value.
         */
        public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                        HttpServletRequest req) {
                String details = "Invalid value for '" + ex.getName() + "': " + ex.getValue();
                ErrorResponse body = ErrorResponse.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .message(details)
                                .path(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body);
        }

        @ExceptionHandler(NoHandlerFoundException.class)
        /**
         * Handle requests for unknown routes and return a JSON 404 payload.
         */
        public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest req) {
                ErrorResponse body = ErrorResponse.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.NOT_FOUND.value())
                                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .message("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL())
                                .path(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body);
        }

        @ExceptionHandler(NumberFormatException.class)
        /**
         * Handle {@link NumberFormatException} and return a 400 payload.
         */
        public ResponseEntity<ErrorResponse> handleNumberFormat(NumberFormatException ex, HttpServletRequest req) {
                ErrorResponse body = ErrorResponse.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .message(ex.getMessage())
                                .path(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(body);
        }
}
