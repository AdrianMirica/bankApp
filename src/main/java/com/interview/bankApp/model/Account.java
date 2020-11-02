package com.interview.bankApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table
public class Account {

    @Id
    @Column
    private long accountId;

    @Column
    private String accountCurrency;

    @Column
    private String accountNumber;

    @Column
    private Double accountValue;

    @Column
    private String accountStatus;

    @OneToMany(mappedBy = "account")
    @JsonManagedReference
    private List<Transaction> transactions;

}
