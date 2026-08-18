package com.loanflow.customer_service.dto;

import java.time.LocalDateTime;

public record ApiError(
    String code,
    String message,
    LocalDateTime timestamp
) {

}
