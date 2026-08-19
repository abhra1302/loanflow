package com.loanflow.loan_service.dto;

import java.time.LocalDateTime;

public record CustomerResponse(
    Long id,
    String name,
    String email,
    String phone,
    String status,
    LocalDateTime createdAt
) {

}
