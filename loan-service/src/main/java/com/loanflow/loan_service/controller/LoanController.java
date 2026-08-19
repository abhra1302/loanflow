package com.loanflow.loan_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.loanflow.loan_service.client.CustomerClient;
import com.loanflow.loan_service.dto.CreateLoanRequest;
import com.loanflow.loan_service.dto.CustomerResponse;
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
    private final CustomerClient customerClient;

    public LoanController(LoanService loanService, CustomerClient customerClient) {
        this.loanService = loanService;
        this.customerClient = customerClient;
    }
    
    @PostMapping()
    public LoanResponse createLoan(@Valid @RequestBody CreateLoanRequest request) {
        return loanService.createLoan(request);
    }
    
    @GetMapping("customer/{customerId}")
    public CustomerResponse getCustomer(@PathVariable Long customerId) {
        return customerClient.getCustomer(customerId);
    }
    

}
