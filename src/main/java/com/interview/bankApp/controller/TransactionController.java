package com.interview.bankApp.controller;

import com.interview.bankApp.exception.AccountNotFoundException;
import com.interview.bankApp.exception.TransactionNotFoundException;
import com.interview.bankApp.model.Transaction;
import com.interview.bankApp.service.AccountService;
import com.interview.bankApp.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @GetMapping("/transactions")
    private List<Transaction> getAllTransactions() {
        logger.info("All transaction are fetched from the database");
        return transactionService.getAllTransactions();
    }

    @GetMapping("/transactions/{id}")
    private Transaction getTransactionById(@PathVariable("id") int id) {
        try {
            return transactionService.getTransactionById(id);
        } catch (TransactionNotFoundException tnfe) {
            logger.error(tnfe.getMessage(), tnfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, tnfe);
        }

    }

    @PostMapping("/transaction")
    private int createTransaction(@RequestBody Transaction transaction ) throws AccountNotFoundException {
        transactionService.createTransaction(transaction);
        logger.info("Transaction with ID = " + transaction.getTransactionId() + " was been created");
        accountService.accountValueUpdateSender(transaction.getTransactionSender());
        accountService.accountValueUpdateReceiver(transaction.getTransactionReceiver());
        logger.info("Account values for sender with accountNumber = " + transaction.getTransactionSender()
                + " and receiver with accountNumber= " + transaction.getTransactionReceiver()
                + " have been updated");

        return transaction.getTransactionId();
    }

    @DeleteMapping("/transaction/delete/{id}")
    private void deleteTransaction(@PathVariable("id") int id) {
        transactionService.deleteTransactionById(id);
        logger.info("Transaction with ID = " + id + " was deleted.");

    }
}
