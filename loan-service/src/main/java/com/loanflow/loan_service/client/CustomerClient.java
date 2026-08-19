package com.loanflow.loan_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.loanflow.loan_service.dto.CustomerResponse;
import com.loanflow.loan_service.exception.CustomerNotFoundException;

@Component
public class CustomerClient {

    private final RestClient restClient;

    public CustomerClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public CustomerResponse getCustomer(Long customerId) {
        try {
            return restClient.get()
                    .uri("/customers/{customerId}", customerId)
                    .retrieve()
                    .body(CustomerResponse.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new CustomerNotFoundException(customerId);
        }
    }
}
