package com.interview.bankApp.service;

import com.interview.bankApp.exception.AccountNotFoundException;
import com.interview.bankApp.model.Account;
import com.interview.bankApp.model.Transaction;
import com.interview.bankApp.repository.AccountRepository;
import com.interview.bankApp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    public List<Account> getAllAccounts() {
        List<Account> accountList = new ArrayList<>();
        accountRepository.findAll().forEach(accountList::add);
        return accountList;
    }

    public Account getAccountById(int id) throws AccountNotFoundException {
        if(accountRepository.findById(id).isPresent())
            return accountRepository.findById(id).get();
        else
            throw new AccountNotFoundException("Account not found in DB");
    }

    public void deleteAccountById(int id){
        accountRepository.deleteById(id);
    }

    public void createOrUpdateAccount(Account account) {
        accountRepository.save(account);
    }

    public void updateAccountStatus(int id, String status) throws AccountNotFoundException {
        if(accountRepository.findById(id).isPresent())
            accountRepository.findById(id).get().setAccountStatus(status);
        else
            throw new AccountNotFoundException("Account not found in DB");
    }

    public List<Transaction> getAllTransactionsFromAnAccount(int id) throws AccountNotFoundException {
         return getAccountById(id).getTransactions();
    }

    public List<Transaction> getAllTransactionsFromAnAccountAfterDate(int id, LocalDateTime desiredDate) throws AccountNotFoundException {
        List<Transaction> transactionList = new ArrayList<>();
        try {
            getAccountById(id).getTransactions().forEach(
                    transaction -> {if(transaction.getTransactionDate().isAfter(desiredDate))
                    transactionList.add(transaction);}
            );
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException("Account not found in DB");
        }
        return transactionList;
    }

    public List<Transaction> getAllTransactionsForAnAccountForToday(int id) throws AccountNotFoundException {
        List<Transaction> transactionList = new ArrayList<>();
        LocalDate today = LocalDateTime.now().toLocalDate();
        try {
            getAccountById(id).getTransactions().forEach(
                    transaction -> {if(transaction.getTransactionDate().toLocalDate().isAfter(today))
                    transactionList.add(transaction);}
            );
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException("Account not found in DB");
        }
        return transactionList;
    }

    public List<Transaction> getAllTransactionsForAnAccountForYesterday(int id) throws AccountNotFoundException {
        List<Transaction> transactionList = new ArrayList<>();
        LocalDate today = LocalDateTime.now().minusDays(1).toLocalDate();
        try {
            getAccountById(id).getTransactions().forEach(
                    transaction -> {if(transaction.getTransactionDate().toLocalDate().isAfter(today))
                    transactionList.add(transaction);}
            );
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException("Account not found in DB");
        }
        return transactionList;
    }

    public List<Transaction> getAllTransactionsForAnAccountForLastHours(int id, long hours) throws AccountNotFoundException {
        List<Transaction> transactionList = new ArrayList<>();
        LocalDate today = LocalDateTime.now().minusHours(hours).toLocalDate();
        try {
            getAccountById(id).getTransactions().forEach(
                    transaction -> {if(transaction.getTransactionDate().toLocalDate().isAfter(today))
                    transactionList.add(transaction);}
            );
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException("Account not found in DB");
        }
        return transactionList;
    }

}
