package com.example.atm.event;

import lombok.Data;

@Data
public class AmountWasDepositedEvent implements Event {

    private Long bankAccountId;
    private Long amount;
}
