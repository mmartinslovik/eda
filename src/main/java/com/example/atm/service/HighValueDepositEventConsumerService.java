package com.example.atm.service;

import com.example.atm.constants.KafkaContants;
import com.example.atm.event.AmountWasDepositedEvent;
import com.example.atm.event.HighValueDepositWasDetectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class HighValueDepositEventConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(HighValueDepositEventConsumerService.class);
    private static final String GROUP_ID = "my-group";
    private final KafkaTemplate<String, HighValueDepositWasDetectedEvent> kafkaTemplate;
    private final Set<Long> processedEventIds = new HashSet<>();

    @Autowired
    public HighValueDepositEventConsumerService(KafkaTemplate<String, HighValueDepositWasDetectedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = KafkaContants.TOPIC, groupId = GROUP_ID)
    public void listen(AmountWasDepositedEvent amountWasDepositedEvent) {
        long eventId = amountWasDepositedEvent.getBankAccountId();

        if (!processedEventIds.contains(eventId)) {
            processedEventIds.add(eventId);
            if (amountWasDepositedEvent.getAmount() > 10000) {
                logger.info("HighValueDepositWasDetected");
                var highValueDepositWasDetectedEvent = new HighValueDepositWasDetectedEvent();
                highValueDepositWasDetectedEvent.setBankAccountId(amountWasDepositedEvent.getBankAccountId());
                highValueDepositWasDetectedEvent.setAmount(amountWasDepositedEvent.getAmount());
                kafkaTemplate.send(KafkaContants.TOPIC, highValueDepositWasDetectedEvent);
            }
        }
    }
}
