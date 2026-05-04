package com.example.de_tai_tt.fooddelivery.exception;

import com.example.de_tai_tt.fooddelivery.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        return build(ex.getErrorCode().status(), ex.getErrorCode().name(), ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return build(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR.name(), "Request validation failed", request, errors);
    }

    @ExceptionHandler({ConstraintViolationException.class, HandlerMethodValidationException.class})
    public ResponseEntity<ErrorResponse> handleConstraint(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR.name(), ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, ErrorCode.BAD_CREDENTIALS.name(), "Invalid username or password", request, Map.of());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED.name(), "Access denied", request, Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR.name(), ex.getMessage(), request, Map.of());
    }

    private ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            String errorCode,
            String message,
            HttpServletRequest request,
            Map<String, String> validationErrors
    ) {
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                status.value(),
                errorCode,
                message,
                request.getRequestURI(),
                validationErrors
        );
        return ResponseEntity.status(status).body(body);
    }
}
