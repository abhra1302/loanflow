package com.loanflow.loan_service.kafka;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoanApprovedEvent(
    Long loanId,
    Long customerId,
    BigDecimal amount,
    LocalDateTime approvedAt
) {}
