package com.loanflow.loan_service.controller;

import org.springframework.web.bind.annotation.RestController;
import com.loanflow.loan_service.dto.CreateLoanRequest;
import com.loanflow.loan_service.dto.LoanResponse;
import com.loanflow.loan_service.dto.PageResponse;
import com.loanflow.loan_service.service.LoanService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody CreateLoanRequest request) {
        LoanResponse loanResponse = loanService.createLoan(request);
        URI location = URI.create("/loans/" + loanResponse.id());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(loanResponse);
    }
    
    @GetMapping("/{loanId}")
    public ResponseEntity<LoanResponse> getLoan(@PathVariable Long loanId) {
        LoanResponse loanResponse = loanService.getLoan(loanId);
        return ResponseEntity.status(HttpStatus.OK).body(loanResponse);
    }

    @GetMapping
    public PageResponse<LoanResponse> getLoans(
        @PageableDefault(size=10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable) {
        return loanService.getLoans(pageable);
    }
    
}
