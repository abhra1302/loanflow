package com.loanflow.loan_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateLoanRequest(
    @NotNull
    Long customerId,
    
    @NotNull
    @DecimalMin("0.01")
    BigDecimal amount,
    
    @NotNull
    @DecimalMin("0.01")
    BigDecimal interestRate,
    
    @NotNull
    @Min(1)
    Integer tenureMonths
) {

}
