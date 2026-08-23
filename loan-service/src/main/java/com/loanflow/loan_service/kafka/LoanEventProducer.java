package com.loanflow.loan_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class LoanEventProducer {

    private static final String TOPIC = "loan-events";

    private final KafkaTemplate<String, LoanApprovedEvent> kafkaTemplate;

    public LoanEventProducer(KafkaTemplate<String, LoanApprovedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishLoanApproved(LoanApprovedEvent event) {
        kafkaTemplate.send(TOPIC,
            event.loanId().toString(),
            event
        );
    }



}
