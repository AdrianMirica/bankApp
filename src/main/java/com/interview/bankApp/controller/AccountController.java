package com.interview.bankApp.controller;

import com.interview.bankApp.exception.AccountNotFoundException;
import com.interview.bankApp.exception.InvalidInputException;
import com.interview.bankApp.model.Account;
import com.interview.bankApp.model.AccountStatus;
import com.interview.bankApp.model.Transaction;
import com.interview.bankApp.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * This is the controller REST controller for the account
 * @author Adrian
 * @version 1.0
 */

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    /**
     * This method returns all the accounts by reaching to the /accounts endpoint using {@code @GetMapping}
     * @return {@code List<Account>}
     */
    @GetMapping("/accounts")
    private List<Account> getAllAccounts() {
        logger.info("List of all accounts is retrieved!");
        return accountService.getAllAccounts();
    }

    /**
     * This method returns a specific account by reaching to the /account/{id} endpoint using {@code @GetMapping}
     * @param id represents the account id
     * @return {@code Account}
     */
    @GetMapping("/accounts/{id}")
    private Account getAccountById(@PathVariable("id") long id) {
        try {
            return accountService.getAccountById(id);
        } catch (AccountNotFoundException anfe) {
            logger.error("Account with ID = " + id + " not found in the database");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        }
    }

    /**
     * This method removes a specific account that is identified by id
     * @param id - long variable that represents the account id
     */
    @DeleteMapping("/accounts/delete/{id}")
    private void deleteAccount(@PathVariable("id") long id) {
        accountService.deleteAccountById(id);
        logger.info("Account with ID = " + id + " was deleted from database.");
    }

    /**
     * This method creates a new account by sending in the body of a {@code PostMapping} the information for the new account in the JSON format
     * @param account - the new account that we want to create
     * @return {@code id} - the account id of the newly created account
     */
    @PostMapping("/account")
    private long createAccount(@RequestBody Account account){
        try {
            accountService.createAccount(account);
        } catch (InvalidInputException iie) {
            logger.error(iie.getMessage(), iie);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have entered some invalid information for this account: " + account.toString());
        }
        logger.info("Account with ID = " + account.getAccountId() + " has been created!");
        return account.getAccountId();
    }

    /**
     * This method updates the account status of the account. The states are being hold in the {@code AccountStatus} enum
     * @param id - represents the account id of the account that we want to changed the status
     */
    @PutMapping("/accounts/status/{id}")
    private void updateAccountStatus(@PathVariable("id") long id) {
        try {
            if(accountService.getAccountById(id).getAccountStatus().equals(AccountStatus.OPEN.toString())) {
                accountService.updateAccountStatus(id, AccountStatus.CLOSED.toString());
                logger.info("Account status for account with ID = " + id + " was changed to " + AccountStatus.CLOSED.toString());
            }
            else
                throw  new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Account is already closed for account with ID = " + id);
        } catch (AccountNotFoundException anfe) {
            logger.error(anfe.getMessage(), anfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        }
    }

    /**
     * This method get all the transactions for a specific account that is identified by id
     * @param id - account id of the account we want to retrieve the transaction list
     * @return {@code List<Transaction>}
     */
    @GetMapping("/accounts/{id}/transactions")
    private List<Transaction> getAllTransactionsFromAnAccount(@PathVariable("id") int id) {
        try {
            logger.info("All transactions for account with ID =" + id + " were fetched");
            return accountService.getAllTransactionsForAnAccount(id);
        } catch (AccountNotFoundException anfe) {
            logger.error(anfe.getMessage(), anfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        }
    }

    /**
     * This method returns all transaction for a specific account identified by {@code id} that were processed after a {@code desiredDate}
     * @param id - account id
     * @param desiredDate - the date after we want to get all the transaction
     * @return {@code List<Transaction>}
     */
    @GetMapping("/accounts/{id}/transactions/{desiredDate}")
    private List<Transaction> getAllTransactionsFromAnAccountAfterDate(@PathVariable("id") int id, @PathVariable("desiredDate") String desiredDate) {
        try {
            return accountService.getAllTransactionsForAnAccountAfterDate(id, desiredDate);
        } catch (AccountNotFoundException anfe) {
            logger.error(anfe.getMessage(), anfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        } catch (InvalidInputException iie) {
            logger.error(iie.getMessage(), iie);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have have entered a date in the future, " + desiredDate + " , please retry with a correct date");
        }
    }

    /**
     * This method returns all the transactions for a specific account that were done today
     * @param id - account id
     * @return {@code List<Transactions>}
     */
    @GetMapping("/accounts/{id}/transactions/today")
    private List<Transaction> getAllTransactionsForAnAccountForToday(@PathVariable("id") int id) {
        try {
            return accountService.getAllTransactionsForAnAccountForToday(id);
        } catch (AccountNotFoundException anfe) {
            logger.error(anfe.getMessage(), anfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        }
    }

    /**
     * This method returns all the transactions for a specific account that were done yesterday
     * @param id - account id
     * @return {@code List<Transactions>}
     */
    @GetMapping("/accounts/{id}/transactions/yesterday")
    private List<Transaction> getAllTransactionsForAnAccountForYesterday(@PathVariable("id") int id) {
        try {
            return accountService.getAllTransactionsForAnAccountForYesterday(id);
        } catch (AccountNotFoundException anfe) {
            logger.error(anfe.getMessage(), anfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        }
    }

    /**
     * This method returns all the transactions for a specific account that were done in the last-{@code hours}
     * @param id - account id
     * @param hours - long parameter used for searching transactions. He should be a positive value.
     * @return {@code List<Transactions>}
     */
    @GetMapping("/accounts/{id}/transactions/last{hours}h")
    private List<Transaction> getAllTransactionsForAnAccountForLastHours(@PathVariable("id") int id, @PathVariable("hours") long hours) {
        try {
            return accountService.getAllTransactionsForAnAccountForLastHours(id, hours);
        } catch (AccountNotFoundException anfe) {
            logger.error(anfe.getMessage(), anfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        } catch (InvalidInputException iie) {
            logger.error(iie.getMessage(), iie);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have entered a invalid parameter, " + hours + " . Hours parameter should be always positive");
        }
    }
}
