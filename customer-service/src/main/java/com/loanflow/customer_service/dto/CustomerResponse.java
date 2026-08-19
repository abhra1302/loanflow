package com.loanflow.customer_service.dto;

import java.time.LocalDateTime;

import com.loanflow.customer_service.enums.CustomerStatus;

public record CustomerResponse(
    Long id,
    String name,
    String email,
    String phone,
    CustomerStatus status,
    LocalDateTime createdAt
) {}
