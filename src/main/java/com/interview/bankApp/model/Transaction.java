package com.interview.bankApp.model;

import lombok.Data;

import java.util.Date;

@Data
public class Transaction {

    private double value;
    private Date timeOfTransaction;
    private String currency;
    private String toAccount;
    private String fromAccount;
}
