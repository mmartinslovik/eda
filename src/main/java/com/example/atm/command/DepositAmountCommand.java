package com.example.atm.command;

import lombok.Data;

@Data
public class DepositAmountCommand implements Command {

    private Long bankAccountId;
    private Long amount;
}
