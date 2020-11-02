package com.interview.bankApp.controller;

import com.interview.bankApp.exception.AccountNotFoundException;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @GetMapping("/accounts")
    private List<Account> getAllAccounts() {
        logger.info("List of all accounts is retrieved!");
        return accountService.getAllAccounts();
    }

    @GetMapping("/account/{id}")
    private Account getAccountById(@PathVariable("id") int id) {
        try {
            return accountService.getAccountById(id);
        } catch (AccountNotFoundException anfe) {
            logger.error("Account with ID = " + id + " not found in the database");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        }
    }

    @DeleteMapping("/account/delete/{id}")
    private void deleteAccount(@PathVariable("id") int id) {
        accountService.deleteAccountById(id);
        logger.info("Account with ID = " + id + " was deleted from database.");
    }

    @PostMapping("/account")
    private long createAccount(@RequestBody Account account){
        accountService.createAccount(account);
        logger.info("Account with ID = " + account.getAccountId() + " has been created!");
        return account.getAccountId();
    }

    @PutMapping("/account/status/{id}")
    private void updateAccountStatus(@PathVariable("id") int id) {
        try {
            if(accountService.getAccountById(id).getAccountStatus().equals(AccountStatus.OPEN.toString())) {
                accountService.updateAccountStatus(id, AccountStatus.CLOSED.name());
                logger.info("Account status for account with ID = " + id + " was changed to " + AccountStatus.CLOSED.toString());
            }
            else
                throw  new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Account is already closed for account with ID = " + id);
        } catch (AccountNotFoundException anfe) {
            logger.error(anfe.getMessage(), anfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        }
    }

    @GetMapping("/account/{id}/transactions")
    private List<Transaction> getAllTransactionsFromAnAccount(@PathVariable("id") int id) {
        try {
            logger.info("All transactions for account with ID =" + id + " were fetched");
            return accountService.getAllTransactionsFromAnAccount(id);
        } catch (AccountNotFoundException anfe) {
            logger.error(anfe.getMessage(), anfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        }
    }

    @GetMapping("/account/{id}/transactions/{desiredDate}")
    private List<Transaction> getAllTransactionsFromAnAccountAfterDate(@PathVariable("id") int id, @PathVariable("desiredDate") LocalDateTime desiredDate) {
        try {
            return accountService.getAllTransactionsFromAnAccountAfterDate(id, desiredDate);
        } catch (AccountNotFoundException anfe) {
            logger.error(anfe.getMessage(), anfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        }
    }

    @GetMapping("/account/{id}/transactions/today")
    private List<Transaction> getAllTransactionsForAnAccountForToday(@PathVariable("id") int id) {
        try {
            return accountService.getAllTransactionsForAnAccountForToday(id);
        } catch (AccountNotFoundException anfe) {
            logger.error(anfe.getMessage(), anfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        }
    }

    @GetMapping("/account/{id}/transactions/yesterday")
    private List<Transaction> getAllTransactionsForAnAccountForYesterday(@PathVariable("id") int id) {
        try {
            return accountService.getAllTransactionsForAnAccountForYesterday(id);
        } catch (AccountNotFoundException anfe) {
            logger.error(anfe.getMessage(), anfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        }
    }

    @GetMapping("/account/{id}/transactions/last{hours}h")
    private List<Transaction> getAllTransactionsForAnAccountForLastHours(@PathVariable("id") int id, @PathVariable("hours") long hours) {
        try {
            return accountService.getAllTransactionsForAnAccountForLastHours(id, hours);
        } catch (AccountNotFoundException anfe) {
            logger.error(anfe.getMessage(), anfe);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found using ID = " + id, anfe);
        }
    }
}
