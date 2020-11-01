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
    private int id;

    @Column
    private String currency;

    @Column
    private String accNumber;

    @Column
    private double amount;

}
