package com.interview.bankApp.controller;

import com.interview.bankApp.model.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        Account a1 = new Account();
        a1.setAccNumber("1234");
        a1.setCurrency("RON");
        Account a2 = new Account();
        a2.setCurrency("EUR");
        a2.setAccNumber("9999");
        list.add(a1);
        list.add(a2);
        return list;
    }
}
