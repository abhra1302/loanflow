package com.loanflow.customer_service.dto;
import com.loanflow.customer_service.enums.CustomerStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCustomerRequest(
    @NotBlank
    String name,
    
    @NotBlank
    @Email
    String email,
    
    @NotBlank
    String phone,
    
    @NotNull
    CustomerStatus status
) {}
