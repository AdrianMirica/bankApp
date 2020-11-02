package com.interview.bankApp.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table
public class Account {

    @Id
    @Column
    private int accountId;

    @Column
    private String accountCurrency;

    @Column
    private String accountNumber;

    @Column
    private double accountValue;

    @Column
    private String accountStatus;

    @OneToMany(targetEntity = Transaction.class)
    private List<Transaction> transactions;

}
