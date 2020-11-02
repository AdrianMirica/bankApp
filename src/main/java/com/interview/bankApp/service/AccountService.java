package com.interview.bankApp.service;

import com.interview.bankApp.model.Account;
import com.interview.bankApp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        List<Account> accountList = new ArrayList<>();
        accountRepository.findAll().forEach(accountList::add);
        return accountList;
    }

    public Account getAccountById(int id) {
        if(accountRepository.findById(id).isPresent())
            return accountRepository.findById(id).get();
        else
            return null; //exception
    }

    public void deleteAccountById(int id){
        accountRepository.deleteById(id);
    }

    public void createOrUpdateAccount(Account account) {
        accountRepository.save(account);
    }

    public void updateAccountStatus(int id, String status){
        if(accountRepository.findById(id).isPresent())
            accountRepository.findById(id).get().setAccountStatus(status);
        //eroare de update
    }
}
