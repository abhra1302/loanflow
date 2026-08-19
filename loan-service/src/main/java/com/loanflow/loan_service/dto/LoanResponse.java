package com.loanflow.loan_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.loanflow.loan_service.enums.LoanStatus;

public record LoanResponse(
    Long id,
    Long customerId,
    BigDecimal amount,
    BigDecimal interestRate,
    Integer tenureMonths,
    LoanStatus status,
    LocalDateTime createdAt
) {

}
