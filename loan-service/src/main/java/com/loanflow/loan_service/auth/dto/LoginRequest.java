package com.loanflow.loan_service.auth.dto;

public record LoginRequest(
        String username,
        String password) {

}
