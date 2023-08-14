package com.example.atm.event;

import lombok.Data;

@Data
public class HighValueDepositWasDetectedEvent implements Event {

    private Long bankAccountId;
    private Long amount;
}
