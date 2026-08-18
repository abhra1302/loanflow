package com.loanflow.customer_service.dto;
import com.loanflow.customer_service.enums.CustomerStatus;

public record CreateCustomerRequest(
    String name,
    String email,
    String phone,
    CustomerStatus status
) {}
