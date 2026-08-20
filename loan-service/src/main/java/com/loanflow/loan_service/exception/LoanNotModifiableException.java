package com.loanflow.loan_service.exception;

public class LoanNotModifiableException extends RuntimeException {
    public LoanNotModifiableException(Long loanId, String currentStatus) {
        super("Loan " + loanId + " cannot be modified in status: " + currentStatus);
    }

}
