package com.interview.bankApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table
@Builder
public class Account {

    @Id
    @Column
    private long accountId;

    @Column
    @NotNull
    private String accountCurrency;

    @Column
    @NotNull
    private String accountNumber;

    @Column
    @NotNull
    private Double accountValue;

    @Column
    @NotNull
    private String accountStatus;

    @OneToMany(mappedBy = "account")
    @JsonManagedReference
    private List<Transaction> transactions;

}
