package com.loanflow.loan_service.exception;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(Long loanId) {
        super("Loan not found with ID: " + loanId);
    }

}
