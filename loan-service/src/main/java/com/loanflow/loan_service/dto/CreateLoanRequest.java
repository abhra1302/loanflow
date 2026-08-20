package com.loanflow.loan_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateLoanRequest(
    @NotNull
    @Positive
    Long customerId,
    
    @NotNull
    @DecimalMin("0.01")
    BigDecimal amount,
    
    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("100.0")
    BigDecimal interestRate,
    
    @NotNull
    @Min(1)
    Integer tenureMonths
) {

}
