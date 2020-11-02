package com.interview.bankApp.controller;

import com.interview.bankApp.exception.AccountNotFoundException;
import com.interview.bankApp.exception.TransactionNotFoundException;
import com.interview.bankApp.model.Transaction;
import com.interview.bankApp.service.AccountService;
import com.interview.bankApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @GetMapping("/transactions")
    private List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/transactions/{id}")
    private Transaction getTransactionById(@PathVariable("id") int id) {
        try {
            return transactionService.getTransactionById(id);
        } catch (TransactionNotFoundException tnfe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, tnfe);
        }

    }

    @PostMapping("/transaction")
    private int createTransaction(@RequestBody Transaction transaction ) throws AccountNotFoundException {
        transactionService.createTransaction(transaction);
        accountService.accountValueUpdateSender(transaction.getTransactionSender());
        accountService.accountValueUpdateReceiver(transaction.getTransactionReceiver());

        return transaction.getTransactionId();
    }

    @DeleteMapping("/transaction/delete/{id}")
    private void deleteTransaction(@PathVariable("id") int id) {transactionService.deleteTransactionById(id);}
}
