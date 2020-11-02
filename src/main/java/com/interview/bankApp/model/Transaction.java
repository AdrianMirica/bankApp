package com.interview.bankApp.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table
public class Transaction {

    @Id
    @Column
    private int transactionId;

    @OneToOne(targetEntity = Account.class)
    private Account account;

    @Column
    private double transactionValue;

    @Column
    private Date transactionDate;

    @Column
    private final String transactionExpeditor = account.getAccountNumber();

    @Column
    private String transactionReceiver;

    @Column
    private String transactionCurrency;
}
