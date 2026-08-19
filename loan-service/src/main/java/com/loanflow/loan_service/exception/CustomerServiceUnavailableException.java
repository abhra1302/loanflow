package com.loanflow.loan_service.exception;

public class CustomerServiceUnavailableException extends RuntimeException {
    public CustomerServiceUnavailableException() {
        super("Customer service is currently unavailable");
    }

}
