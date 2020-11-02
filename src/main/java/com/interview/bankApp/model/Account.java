package com.interview.bankApp.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

}
