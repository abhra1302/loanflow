package com.loanflow.loan_service.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.loanflow.loan_service.dto.ApiError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiError> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ApiError apiError = new ApiError("CUSTOMER_NOT_FOUND", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
        String messageString = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                .orElse("Validation failed");
        ApiError apiError = new ApiError("VALIDATION_ERROR", messageString, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(CustomerServiceUnavailableException.class)
    public ResponseEntity<ApiError> handleCustomerServiceUnavailableException(CustomerServiceUnavailableException ex) {
        ApiError apiError = new ApiError("CUSTOMER_SERVICE_UNAVAILABLE", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(apiError);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<ApiError> handleLoanNotFoundException(LoanNotFoundException ex) {
        ApiError apiError = new ApiError("LOAN_NOT_FOUND", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(LoanNotModifiableException.class)
    public ResponseEntity<ApiError> handleLoanNotModifiable(
            LoanNotModifiableException ex) {
        ApiError apiError = new ApiError("LOAN_NOT_MODIFIABLE", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(apiError);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> handleOptimisticLocking(
            ObjectOptimisticLockingFailureException ex) {
        ApiError apiError = new ApiError("CONCURRENT_MODIFICATION", "The loan was modified by another request. Please retry.", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {
        ApiError apiError = new ApiError("DATA_INTEGRITY_VIOLATION", "The request could not be completed because it violates a data constraint.", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

}
