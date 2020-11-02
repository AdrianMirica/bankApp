package com.interview.bankApp.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table
public class Transaction {

    @Id
    @Column
    private int transactionId;

    @Column
    private Double transactionValue;

    @Column
    private LocalDateTime transactionDate;

    @Column
    private String transactionSender;

    @Column
    private String transactionReceiver;

    @Column
    private String transactionCurrency;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;
}
