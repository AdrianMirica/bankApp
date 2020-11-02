package com.interview.bankApp.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table
public class Transaction {

    @Id
    @Column
    private int transactionId;

    @Column
    private Account account;

    @Column
    private double transactionValue;

    @Column
    private Date transactionDate;

    @Column
    private String transactionExpeditor;

    @Column
    private String transactionReceiver;

    @Column
    private String transactionCurrency;
}
