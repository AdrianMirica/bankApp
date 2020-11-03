package com.interview.bankApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table
@Builder
public class Transaction {

    @Id
    @Column
    private long transactionId;

    @Column
    @NotNull
    private Double transactionValue;

    @Column
    @NotNull
    private LocalDateTime transactionDate;

    @Column
    @NotNull
    private String transactionSender;

    @Column
    @NotNull
    private String transactionReceiver;

    @Column
    @NotNull
    private String transactionCurrency;

    @ManyToOne
    @JoinColumn(name = "accountId")
    @JsonBackReference
    private Account account;
}
