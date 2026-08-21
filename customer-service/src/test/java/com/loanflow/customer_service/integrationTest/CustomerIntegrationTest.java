package com.loanflow.customer_service.integrationTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.autoconfigure.ServerProperties.Servlet;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.loanflow.customer_service.repository.CustomerRepository;

import jakarta.servlet.ServletException;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

     @Test 
    void shouldCreateCustomerRollback() throws Exception { 
               
        String request = """
                        {
                    "name": "Test Customer",
                    "email": "dgfgdfg@something.com",
                    "phone": "9999999999",
                    "status": "ACTIVE"
                }
                        """;
        assertThrows(ServletException.class, 
            () -> mockMvc.perform(MockMvcRequestBuilders.post("/customers/rollback") 
            .contentType(MediaType.APPLICATION_JSON) 
            .content(request))
        );
        // mockMvc.perform(MockMvcRequestBuilders.post("/customers/rollback") 
        // .contentType(MediaType.APPLICATION_JSON) 
        // .content(request)
        // ).andExpect(result -> {
        //     assertTrue(
        //             result.getResponse().getStatus() >= 500,
        //             "Expected transaction failure"
        //     );
        // });

        assertTrue(
                customerRepository.findAll().stream()
                        .noneMatch(customer ->
                                customer.getEmail()
                                        .equals("rollback-test@something.com")
                        ),
                "Customer should have been rolled back"
        );
    
    }

}
