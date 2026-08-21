package com.loanflow.customer_service.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanflow.customer_service.dto.CreateCustomerRequest;
import com.loanflow.customer_service.dto.CustomerResponse;
import com.loanflow.customer_service.entity.Customer;
import com.loanflow.customer_service.exception.CustomerNotFoundException;
import com.loanflow.customer_service.repository.CustomerRepository;

@Service
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        return new CustomerResponse(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getStatus(),
            customer.getCreatedAt()
        );
    }

    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setStatus(request.status());
        customer.setCreatedAt(LocalDateTime.now());
        Customer savedCustomer = customerRepository.save(customer);
        return new CustomerResponse(
            savedCustomer.getId(),
            savedCustomer.getName(),
            savedCustomer.getEmail(),
            savedCustomer.getPhone(),
            savedCustomer.getStatus(),
            savedCustomer.getCreatedAt()
        );
    }

    @Transactional
    public void operationThatShouldRollback(CreateCustomerRequest request) throws RuntimeException {

        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setStatus(request.status());
        customer.setCreatedAt(LocalDateTime.now());
        Customer savedCustomer = customerRepository.save(customer);
        savedCustomer.setName("Updated Rollback Test");

        customerRepository.save(savedCustomer);

        throw new RuntimeException("Simulated failure");
    }
}
