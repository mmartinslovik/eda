package com.example.atm.rest;

import com.example.atm.command.DepositAmountCommand;
import com.example.atm.constants.KafkaContants;
import com.example.atm.event.AmountWasDepositedEvent;
import com.example.atm.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AtmController {

    private static final Logger logger = LoggerFactory.getLogger(AtmController.class);
    private final KafkaTemplate<String, Event> kafkaTemplate;

    @Autowired
    public AtmController(KafkaTemplate<String, Event> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping(value = "/api/deposit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> depositAmount(@RequestBody DepositAmountCommand depositAmountCommand) {
        var amountWasDepositedEvent = new AmountWasDepositedEvent();
        amountWasDepositedEvent.setBankAccountId(depositAmountCommand.getBankAccountId());
        amountWasDepositedEvent.setAmount(depositAmountCommand.getAmount());
        kafkaTemplate.send(KafkaContants.TOPIC, amountWasDepositedEvent);
        logger.info(amountWasDepositedEvent.toString());
        return ResponseEntity.ok(amountWasDepositedEvent.toString());
    }
}
