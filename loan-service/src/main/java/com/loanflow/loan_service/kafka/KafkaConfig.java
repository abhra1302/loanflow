package com.loanflow.loan_service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic loanEventsTopic() {
        return new NewTopic("loan-events", 3, (short) 1);
    }
}
