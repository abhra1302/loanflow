package com.loanflow.loan_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanflow.loan_service.client.CustomerClient;
import com.loanflow.loan_service.dto.CreateLoanRequest;
import com.loanflow.loan_service.dto.LoanResponse;
import com.loanflow.loan_service.dto.PageResponse;
import com.loanflow.loan_service.dto.UpdateLoanRequest;
import com.loanflow.loan_service.entity.Loan;
import com.loanflow.loan_service.enums.LoanStatus;
import com.loanflow.loan_service.exception.LoanNotFoundException;
import com.loanflow.loan_service.exception.LoanNotModifiableException;
import com.loanflow.loan_service.kafka.LoanApprovedEvent;
import com.loanflow.loan_service.kafka.LoanEventProducer;
import com.loanflow.loan_service.mapper.LoanMapper;
import com.loanflow.loan_service.repository.LoanRepository;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final CustomerClient customerClient;
    private final LoanMapper loanMapper;
    private final LoanEventProducer loanEventProducer;

    public LoanService(LoanRepository loanRepository, CustomerClient customerClient, LoanMapper loanMapper, LoanEventProducer loanEventProducer) {
        this.loanRepository = loanRepository;
        this.customerClient = customerClient;
        this.loanMapper = loanMapper;
        this.loanEventProducer = loanEventProducer;
    }

    public LoanResponse createLoan(CreateLoanRequest request) {
        customerClient.getCustomer(request.customerId());
        // Loan loan = new Loan();
        // loan.setCustomerId(request.customerId());
        // loan.setAmount(request.amount());
        // loan.setInterestRate(request.interestRate());
        // loan.setTenureMonths(request.tenureMonths());
        Loan loan = loanMapper.toEntity(request);
        // loan.setStatus(LoanStatus.PENDING);
        // loan.setCreatedAt(LocalDateTime.now());
        Loan saved = loanRepository.save(loan);
        // return new LoanResponse(
        // saved.getId(),
        // saved.getCustomerId(),
        // saved.getAmount(),
        // saved.getInterestRate(),
        // saved.getTenureMonths(),
        // saved.getStatus(),
        // saved.getCreatedAt()
        // );
        return loanMapper.toResponse(saved);
    }

    @Transactional
    public LoanResponse getLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
        // return new LoanResponse(
        // loan.getId(),
        // loan.getCustomerId(),
        // loan.getAmount(),
        // loan.getInterestRate(),
        // loan.getTenureMonths(),
        // loan.getStatus(),
        // loan.getCreatedAt()
        // );
        return loanMapper.toResponse(loan);
    }

    public PageResponse<LoanResponse> getLoans(Pageable pageable) {
        Page<LoanResponse> page = loanRepository.findAll(pageable)
                .map(loanMapper::toResponse);
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getTotalPages());
    }

    public LoanResponse updateLoan(Long loanId, UpdateLoanRequest request) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new LoanNotFoundException(loanId));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new LoanNotModifiableException(loanId, loan.getStatus().name());
        }

        loan.setAmount(request.amount());
        loan.setInterestRate(request.interestRate());
        loan.setTenureMonths(request.tenureMonths());
        Loan updatedLoan = loanRepository.save(loan);
        return loanMapper.toResponse(updatedLoan);
    }

    public LoanResponse approveLoan(Long loanId) {
        Loan loan = updateStatus(loanId, LoanStatus.APPROVED);

        LoanApprovedEvent event = new LoanApprovedEvent(
            loan.getId(),
            loan.getCustomerId(),
            loan.getAmount(),
            LocalDateTime.now()
        );
        loanEventProducer.publishLoanApproved(event);
        
        return loanMapper.toResponse(loan);

    }

    public LoanResponse rejectLoan(Long loanId) {
        return loanMapper.toResponse(
                updateStatus(loanId, LoanStatus.REJECTED));
    }

    @Transactional
    private Loan updateStatus(Long loanId, LoanStatus newStatus) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        // if (loan.getStatus() != LoanStatus.PENDING) {
        // throw new LoanNotModifiableException(
        // loanId,
        // loan.getStatus().name());
        // }

        LoanStatus currentStatus = loan.getStatus();

        boolean validTransition = switch (newStatus) {
            case LoanStatus.APPROVED, LoanStatus.REJECTED, LoanStatus.CANCELLED -> currentStatus == LoanStatus.PENDING;
            case LoanStatus.DISBURSED -> currentStatus == LoanStatus.APPROVED;
            default -> false;
        };

        if (!validTransition) {
            throw new LoanNotModifiableException(
                    loanId,
                    loan.getStatus().name());
        }
        ;

        loan.setStatus(newStatus);

        return loanRepository.save(loan);
    }

    public LoanResponse cancelLoan(Long loanId) {
        return loanMapper.toResponse(
                updateStatus(loanId, LoanStatus.CANCELLED));
    }

    public LoanResponse disburseLoan(Long loanId) {
        return loanMapper.toResponse(
                updateStatus(loanId, LoanStatus.DISBURSED));
    }

    @Transactional
    public void updateLoanWithFailure(Long loanId, UpdateLoanRequest request) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new LoanNotModifiableException(
                    loanId,
                    loan.getStatus().name());
        }

        // First database change
        loan.setAmount(request.amount());
        loan.setInterestRate(request.interestRate());
        loan.setTenureMonths(request.tenureMonths());

        loanRepository.save(loan);

        // Simulate something failing AFTER the update
        throw new RuntimeException("Simulated failure");
    }

}
