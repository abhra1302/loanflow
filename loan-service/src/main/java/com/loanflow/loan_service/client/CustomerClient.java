package com.loanflow.loan_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import com.loanflow.loan_service.dto.CustomerResponse;
import com.loanflow.loan_service.exception.CustomerNotFoundException;
import com.loanflow.loan_service.exception.CustomerServiceUnavailableException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component
public class CustomerClient {

    private final RestClient restClient;

    public CustomerClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Retry(name = "customerService")
    @CircuitBreaker(name = "customerService") // , fallbackMethod = "customerServiceFallback"
    public CustomerResponse getCustomer(Long customerId) {
        // Supplier<CustomerResponse> customerSupplier = () -> restClient.get()
        // .uri("/customers/{customerId}", customerId)
        // .retrieve()
        // .body(CustomerResponse.class);
        // Supplier<CustomerResponse> decoratedCustomerSupplier =
        // Decorators.ofSupplier(customerSupplier)
        // .withRetry(retry)
        // .withCircuitBreaker(circuitBreaker)
        // .decorate();
        // try {
        // return decoratedCustomerSupplier.get();
        // } catch (HttpClientErrorException.NotFound ex) {
        // throw new CustomerNotFoundException(customerId);
        // } catch (CallNotPermittedException ex) {
        // throw new RuntimeException("Customer service circuit is open");
        // }
        try {
            return restClient.get()
                    .uri("/customers/{customerId}", customerId)
                    .retrieve()
                    .body(CustomerResponse.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new CustomerNotFoundException(customerId);
        } catch (ResourceAccessException ex) {
            throw new CustomerServiceUnavailableException();
        }
    }

    // @SuppressWarnings("unused") // Called at runtime by Resilience4j
    // private CustomerResponse customerServiceFallback(
    //         Long customerId,
    //         Throwable throwable) {

    //     // throw new RuntimeException(
    //     // "Customer service is currently unavailable");

    //     if (throwable instanceof CustomerNotFoundException) {
    //         throw (CustomerNotFoundException) throwable;
    //     }

    //     throw new CustomerServiceUnavailableException();
    // }
}
