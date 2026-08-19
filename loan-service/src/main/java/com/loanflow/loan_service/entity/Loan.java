package com.loanflow.loan_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.loanflow.loan_service.enums.LoanStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;
    
    @Column(name = "amount")
    private BigDecimal amount;
    
    @Column(name = "interest_rate")
    private BigDecimal interestRate;
    
    @Column(name = "tenure_months")
    private Integer tenureMonths;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
