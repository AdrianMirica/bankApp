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

/**
 * This is the REST controlloer that handles the transactions
 * @author Adrian
 * @version 1.0
 */
@RestController
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    /**
     * This method returns all transactions using a {@code @GetMapping} on the /transactions endpoint
     * @return {@code List<Transaction>}
     */
    @GetMapping("/transactions")
    private List<Transaction> getAllTransactions() {
        logger.info("All transaction are fetched from the database");
        return transactionService.getAllTransactions();
    }

    /**
     * This method returns a specific transaction based on the transaction id
     * @param id - long variable used to identify the transaction
     * @return {@code Transaction}
     */
    @GetMapping("/transactions/{id}")
    private Transaction getTransactionById(@PathVariable("id") long id) {
        try {
            return transactionService.getTransactionById(id);
        } catch (TransactionNotFoundException tnfe) {
            logger.error(tnfe.getMessage(), tnfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, tnfe);
        }

    }

    /**
     * This method creates a new transaction using a {@code @PostMapping} that has in the body of the request the information about the transaction
     * @param transaction - the information about transaction that is send into the body of the request in JSON format
     * @return {@code id} - the id of the newly created transaction
     * @throws AccountNotFoundException
     */
    @PostMapping("/transaction")
    private long createTransaction(@RequestBody Transaction transaction ) throws AccountNotFoundException {
        transactionService.createTransaction(transaction);
        logger.info("Transaction with ID = " + transaction.getTransactionId() + " was been created");
        accountService.accountValueUpdateSender(transaction.getTransactionSender());
        accountService.accountValueUpdateReceiver(transaction.getTransactionReceiver());
        logger.info("Account values for sender with accountNumber = " + transaction.getTransactionSender()
                + " and receiver with accountNumber= " + transaction.getTransactionReceiver()
                + " have been updated");

        return transaction.getTransactionId();
    }

    /**
     * This method deletes a specific transaction identified by the transaction id
     * @param id - used to identify the transaction that we want to delete
     */
    @DeleteMapping("/transactions/delete/{id}")
    private void deleteTransaction(@PathVariable("id") int id) {
        transactionService.deleteTransactionById(id);
        logger.info("Transaction with ID = " + id + " was deleted.");

    }
}
