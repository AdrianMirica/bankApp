package com.interview.bankApp.controller;

import com.interview.bankApp.model.Transaction;
import com.interview.bankApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("/transactions")
    private List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/transactions/{accountNumber}")
    private List<Transaction> getAllTransactionsForAnAccount(@PathVariable("accountNumber") String accountNumber) {
        return transactionService.getAllTransactionsForAnAccount(accountNumber);
    }

    @GetMapping("/transactions/{accountNumber}/{desiredDate}")
    private List<Transaction> getAllTransactionsForAnAccountAfterDate(@PathVariable("accountNumber") String accountNumber,
                                                                      @PathVariable("desiredDate") Date desiredDate) {
        return transactionService.getAllTransactionsForAnAccountAfterDate(accountNumber,desiredDate);
    }

    @GetMapping("/transactions/{id}")
    private Transaction getTransactionById(@PathVariable("id") int id) {
        return transactionService.getTransactionById(id);

    }

    @PostMapping("/transaction")
    private int createTransaction(@RequestBody Transaction transaction ){
        transactionService.createTransaction(transaction);
        return transaction.getTransactionId();
    }
}
