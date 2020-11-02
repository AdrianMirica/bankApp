package com.interview.bankApp.controller;

import com.interview.bankApp.exception.TransactionNotFoundException;
import com.interview.bankApp.model.Transaction;
import com.interview.bankApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

//    @GetMapping("/transactions/{accountNumber}")
//    private List<Transaction> getAllTransactionsForAnAccount(@PathVariable("accountNumber") String accountNumber) {
//        return transactionService.getAllTransactionsForAnAccount(accountNumber);
//    }

//    @GetMapping("/transactions/{accountNumber}/{desiredDate}")
//    private List<Transaction> getAllTransactionsForAnAccountAfterDate(@PathVariable("accountNumber") String accountNumber,
//                                                                      @PathVariable("desiredDate") Date desiredDate) {
//        return transactionService.getAllTransactionsForAnAccountAfterDate(accountNumber,desiredDate);
//    }

    @GetMapping("/transactions/{id}")
    private Transaction getTransactionById(@PathVariable("id") int id) {
        try {
            return transactionService.getTransactionById(id);
        } catch (TransactionNotFoundException tnfe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, tnfe);
        }

    }

    @PostMapping("/transaction")
    private int createTransaction(@RequestBody Transaction transaction ){
        transactionService.createTransaction(transaction);
        return transaction.getTransactionId();
    }

    @DeleteMapping("/transaction/delete/{id}")
    private void deleteTransaction(@PathVariable("id") int id) {transactionService.deleteTransactionById(id);}
}
