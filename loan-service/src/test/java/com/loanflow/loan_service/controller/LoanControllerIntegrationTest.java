package com.loanflow.loan_service.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.loanflow.loan_service.client.CustomerClient;
import com.loanflow.loan_service.dto.CreateLoanRequest;
import com.loanflow.loan_service.dto.CustomerResponse;
import com.loanflow.loan_service.dto.LoanResponse;
import com.loanflow.loan_service.enums.LoanStatus;
 

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoanControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerClient customerClient;
    
    @Test 
    void shouldCreateLoan() throws Exception { 
        CustomerResponse customer = new CustomerResponse( 10L, "Test Customer", "test@example.com", "9999999999", "ACTIVE", LocalDateTime.now() );
        when(customerClient.getCustomer(anyLong())).thenReturn(customer);
        
        String request = """ 
        { 
        "customerId": 10, 
        "amount": 50000.00, 
        "interestRate": 8.50, 
        "tenureMonths": 60 
        } 
        """; 
        mockMvc.perform(post("/loans") 
        .contentType(MediaType.APPLICATION_JSON) 
        .content(request)) 
        .andExpect(status().isCreated()) 
        .andExpect(jsonPath("$.id").exists()) 
        .andExpect(jsonPath("$.customerId").value(10)) 
        .andExpect(jsonPath("$.amount").value(50000.00)) 
        .andExpect(jsonPath("$.interestRate").value(8.50)) 
        .andExpect(jsonPath("$.tenureMonths").value(60)) 
        .andExpect(jsonPath("$.status").value("PENDING")); 
    }

}
