package com.loanflow.loan_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.loanflow.loan_service.client.CustomerClient;
import com.loanflow.loan_service.dto.CreateLoanRequest;
import com.loanflow.loan_service.dto.LoanResponse;
import com.loanflow.loan_service.dto.UpdateLoanRequest;
import com.loanflow.loan_service.entity.Loan;
import com.loanflow.loan_service.enums.LoanStatus;
import com.loanflow.loan_service.exception.LoanNotFoundException;
import com.loanflow.loan_service.exception.LoanNotModifiableException;
import com.loanflow.loan_service.mapper.LoanMapper;
import com.loanflow.loan_service.repository.LoanRepository;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

        @Mock
        private LoanRepository loanRepository;

        @Mock
        private CustomerClient customerClient;

        @Mock
        private LoanMapper loanMapper;

        @InjectMocks
        private LoanService loanService;

        private Loan loan;

        @BeforeEach
        void setUp() {
                loan = new Loan();
                loan.setId(1L);
                loan.setCustomerId(10L);
                loan.setAmount(new BigDecimal("50000.00"));
                loan.setInterestRate(new BigDecimal("8.50"));
                loan.setTenureMonths(60);
                loan.setStatus(LoanStatus.PENDING);
        }

        @Test
        void createLoan_shouldCreateLoanSuccessfully() {

                CreateLoanRequest request = new CreateLoanRequest(
                                10L,
                                new BigDecimal("50000.00"),
                                new BigDecimal("8.50"),
                                60);

                LoanResponse response = new LoanResponse(
                                1L,
                                10L,
                                new BigDecimal("50000.00"),
                                new BigDecimal("8.50"),
                                60,
                                LoanStatus.PENDING,
                                null,
                                null);

                when(loanMapper.toEntity(request)).thenReturn(loan);
                when(loanRepository.save(loan)).thenReturn(loan);
                when(loanMapper.toResponse(loan)).thenReturn(response);

                LoanResponse result = loanService.createLoan(request);

                assertEquals(1L, result.id());
                assertEquals(LoanStatus.PENDING, result.status());

                verify(customerClient).getCustomer(10L);
                verify(loanRepository).save(loan);
                verify(loanMapper).toEntity(request);
                verify(loanMapper).toResponse(loan);
        }

        @Test
        void getLoan_shouldReturnLoan_whenLoanExists() {

                LoanResponse response = new LoanResponse(
                                1L,
                                10L,
                                new BigDecimal("50000.00"),
                                new BigDecimal("8.50"),
                                60,
                                LoanStatus.PENDING,
                                null,
                                null);

                when(loanRepository.findById(1L))
                                .thenReturn(Optional.of(loan));

                when(loanMapper.toResponse(loan))
                                .thenReturn(response);

                LoanResponse result = loanService.getLoan(1L);

                assertEquals(1L, result.id());
                assertEquals(LoanStatus.PENDING, result.status());

                verify(loanRepository).findById(1L);
                verify(loanMapper).toResponse(loan);
        }

        @Test
        void getLoan_shouldThrowException_whenLoanDoesNotExist() {

                when(loanRepository.findById(999L))
                                .thenReturn(Optional.empty());

                assertThrows(
                                LoanNotFoundException.class,
                                () -> loanService.getLoan(999L));

                verify(loanRepository).findById(999L);
                verify(loanMapper, never()).toResponse(any());
        }

        @Test
        void updateLoan_shouldUpdatePendingLoan() {

                UpdateLoanRequest request = new UpdateLoanRequest(
                                new BigDecimal("60000.00"),
                                new BigDecimal("9.00"),
                                48);

                LoanResponse response = new LoanResponse(
                                1L,
                                10L,
                                new BigDecimal("60000.00"),
                                new BigDecimal("9.00"),
                                48,
                                LoanStatus.PENDING,
                                null,
                                null);

                when(loanRepository.findById(1L))
                                .thenReturn(Optional.of(loan));

                when(loanRepository.save(loan))
                                .thenReturn(loan);

                when(loanMapper.toResponse(loan))
                                .thenReturn(response);

                LoanResponse result = loanService.updateLoan(1L, request);

                assertEquals(new BigDecimal("60000.00"), loan.getAmount());
                assertEquals(new BigDecimal("9.00"), loan.getInterestRate());
                assertEquals(48, loan.getTenureMonths());

                assertEquals(LoanStatus.PENDING, result.status());

                verify(loanRepository).save(loan);
        }

        @Test
        void updateLoan_shouldRejectApprovedLoan() {

                loan.setStatus(LoanStatus.APPROVED);

                when(loanRepository.findById(1L))
                                .thenReturn(Optional.of(loan));

                assertThrows(
                                LoanNotModifiableException.class,
                                () -> loanService.updateLoan(
                                                1L,
                                                new UpdateLoanRequest(
                                                                new BigDecimal("60000.00"),
                                                                new BigDecimal("9.00"),
                                                                48)));

                verify(loanRepository, never()).save(any());
        }

        @Test
        void approveLoan_shouldChangePendingToApproved() {

                when(loanRepository.findById(1L))
                                .thenReturn(Optional.of(loan));

                when(loanRepository.save(loan))
                                .thenReturn(loan);

                LoanResponse response = new LoanResponse(
                                1L,
                                10L,
                                loan.getAmount(),
                                loan.getInterestRate(),
                                loan.getTenureMonths(),
                                LoanStatus.APPROVED,
                                null,
                                null);

                when(loanMapper.toResponse(loan))
                                .thenReturn(response);

                LoanResponse result = loanService.approveLoan(1L);

                assertEquals(LoanStatus.APPROVED, loan.getStatus());
                assertEquals(LoanStatus.APPROVED, result.status());

                verify(loanRepository).save(loan);
        }

        @Test
        void approveLoan_shouldRejectAlreadyApprovedLoan() {

                loan.setStatus(LoanStatus.APPROVED);

                when(loanRepository.findById(1L))
                                .thenReturn(Optional.of(loan));

                assertThrows(
                                LoanNotModifiableException.class,
                                () -> loanService.approveLoan(1L));

                verify(loanRepository, never()).save(any());
        }

        @Test
        void cancelLoan_shouldCancelPendingLoan() {

                when(loanRepository.findById(1L))
                                .thenReturn(Optional.of(loan));

                when(loanRepository.save(loan))
                                .thenReturn(loan);

                LoanResponse response = new LoanResponse(
                                1L,
                                10L,
                                loan.getAmount(),
                                loan.getInterestRate(),
                                loan.getTenureMonths(),
                                LoanStatus.CANCELLED,
                                null,
                                null);

                when(loanMapper.toResponse(loan))
                                .thenReturn(response);

                LoanResponse result = loanService.cancelLoan(1L);

                assertEquals(LoanStatus.CANCELLED, loan.getStatus());
                assertEquals(LoanStatus.CANCELLED, result.status());

                verify(loanRepository).save(loan);
        }

        @Test
        void shouldRollbackLoanUpdateWhenTransactionFails() {

                Long loanId = 1L;

                Loan loan = new Loan();
                loan.setId(loanId);
                loan.setCustomerId(1L);
                loan.setAmount(new BigDecimal("50000.00"));
                loan.setInterestRate(new BigDecimal("8.50"));
                loan.setTenureMonths(60);
                loan.setStatus(LoanStatus.PENDING);

                when(loanRepository.findById(loanId))
                                .thenReturn(Optional.of(loan));

                UpdateLoanRequest request = new UpdateLoanRequest(
                                new BigDecimal("60000.00"),
                                new BigDecimal("9.00"),
                                48);

                assertThrows(
                                RuntimeException.class,
                                () -> loanService.updateLoanWithFailure(loanId, request));
        }
}
