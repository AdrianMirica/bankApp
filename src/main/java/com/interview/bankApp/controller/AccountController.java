package com.interview.bankApp.controller;

import com.interview.bankApp.model.Account;
import com.interview.bankApp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    private List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/account/{id}")
    private Account getAccountById(@PathVariable("id") int id) {
        return accountService.getAccountById(id);
    }

    @DeleteMapping("/account/delete/{id}")
    private void deleteAccount(@PathVariable("id") int id) {
        accountService.deleteAccountById(id);
    }

    @PostMapping("/account")
    private int createAccount(@RequestBody Account account){
        accountService.createOrUpdateAccount(account);
        return account.getAccountId();
    }

    @PutMapping("/account/status/{id}")
    private void updateAccountStatus(@PathVariable("id") int id, String status) {
        accountService.updateAccountStatus(id, status);
    }
}
