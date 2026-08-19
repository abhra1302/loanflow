package com.loanflow.loan_service.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.loanflow.loan_service.client.CustomerClient;
import com.loanflow.loan_service.dto.CreateLoanRequest;
import com.loanflow.loan_service.dto.LoanResponse;
import com.loanflow.loan_service.entity.Loan;
import com.loanflow.loan_service.enums.LoanStatus;
import com.loanflow.loan_service.repository.LoanRepository;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final CustomerClient customerClient;

    public LoanService(LoanRepository loanRepository, CustomerClient customerClient) {
        this.loanRepository = loanRepository;
        this.customerClient = customerClient;
    }

    public LoanResponse createLoan(CreateLoanRequest request) {
        customerClient.getCustomer(request.customerId());
        Loan loan = new Loan();
        loan.setCustomerId(request.customerId());
        loan.setAmount(request.amount());
        loan.setInterestRate(request.interestRate());
        loan.setTenureMonths(request.tenureMonths());
        loan.setStatus(LoanStatus.PENDING);
        loan.setCreatedAt(LocalDateTime.now());
        Loan saved = loanRepository.save(loan);
        return new LoanResponse(
            saved.getId(),
            saved.getCustomerId(),
            saved.getAmount(),
            saved.getInterestRate(),
            saved.getTenureMonths(),
            saved.getStatus(),
            saved.getCreatedAt()
        );
    }

}
