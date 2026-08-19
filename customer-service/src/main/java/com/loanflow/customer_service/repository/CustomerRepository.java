package com.loanflow.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loanflow.customer_service.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    
}
