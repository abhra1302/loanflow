package com.loanflow.loan_service.controller;

import org.springframework.web.bind.annotation.RestController;
import com.loanflow.loan_service.dto.CreateLoanRequest;
import com.loanflow.loan_service.dto.LoanResponse;
import com.loanflow.loan_service.service.LoanService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/loans")   
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }
    
    @PostMapping()
    public LoanResponse createLoan(@Valid @RequestBody CreateLoanRequest request) {
        return loanService.createLoan(request);
    }
    
    @GetMapping("/{loanId}")
    public LoanResponse getLoan(@PathVariable Long loanId) {
        return loanService.getLoan(loanId);
    }
    
}
