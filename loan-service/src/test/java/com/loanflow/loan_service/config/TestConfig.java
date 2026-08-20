package com.loanflow.loan_service.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.loanflow.loan_service.mapper.LoanMapper;
import com.loanflow.loan_service.mapper.LoanMapperImpl;

@TestConfiguration
@ComponentScan(basePackages = {
    "com.loanflow.loan_service.mapper",
    "com.loanflow.loan_service.service",
    "com.loanflow.loan_service.repository",
    "com.loanflow.loan_service.controller",
    "com.loanflow.loan_service.client",
})
public class TestConfig {
    
    // @Bean
    // public LoanMapper loanMapper() {
    //     return new LoanMapperImpl();
    // }
}
