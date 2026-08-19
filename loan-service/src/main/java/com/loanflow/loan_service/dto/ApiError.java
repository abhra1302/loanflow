package com.loanflow.loan_service.dto;

import java.time.LocalDateTime;

public record ApiError(
    String code,
    String message,
    LocalDateTime timestamp
) {

}
