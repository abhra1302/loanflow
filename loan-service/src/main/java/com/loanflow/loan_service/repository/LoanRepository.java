package com.loanflow.loan_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loanflow.loan_service.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

}
