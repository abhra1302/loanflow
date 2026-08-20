package com.loanflow.loan_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.loanflow.loan_service.dto.CreateLoanRequest;
import com.loanflow.loan_service.dto.LoanResponse;
import com.loanflow.loan_service.entity.Loan;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    
    LoanResponse toResponse(Loan loan);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Loan toEntity(CreateLoanRequest request);
}
