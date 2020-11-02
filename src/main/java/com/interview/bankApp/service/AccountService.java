package com.interview.bankApp.service;

import com.interview.bankApp.exception.AccountNotFoundException;
import com.interview.bankApp.model.Account;
import com.interview.bankApp.model.Transaction;
import com.interview.bankApp.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public Account getAccountById(int id) throws AccountNotFoundException {
        if(accountRepository.findById(id).isPresent())
            return accountRepository.findById(id).get();
        else
            throw new AccountNotFoundException("Account with ID = " + id + "was not found");
    }

    public List<Account> getAccountByAccountNumber(String accNumber) {
        List<Account> accountList = new ArrayList<>();
        accountRepository.findAll().forEach(account -> {
            if(account.getAccountNumber().equals(accNumber)) {
                accountList.add(account);
            }
        });
        return accountList;
    }

    public void deleteAccountById(int id){
        accountRepository.deleteById(id);
    }

    public void createAccount(Account account) {
        accountRepository.save(account);
    }

    public void updateAccountStatus(int id, String status) throws AccountNotFoundException {
        if(accountRepository.findById(id).isPresent()) {
            accountRepository.findById(id).get().setAccountStatus(status);
            accountRepository.save(accountRepository.findById(id).get());
        }
        else
            throw new AccountNotFoundException("Account with ID = " + id + "was not found");
    }

    public void updateAccountValue(int id, double newValue) throws AccountNotFoundException {
        if(accountRepository.findById(id).isPresent()) {
            accountRepository.findById(id).get().setAccountValue(newValue);
            accountRepository.save(accountRepository.findById(id).get());
        }
        else
            throw new AccountNotFoundException("Account with ID = " + id + "was not found");
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
            throw new AccountNotFoundException("Account with ID = " + id + "was not found");
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
            throw new AccountNotFoundException("Account with ID = " + id + "was not found");
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
            throw new AccountNotFoundException("Account with ID = " + id + "was not found");
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
            throw new AccountNotFoundException("Account with ID = " + id + "was not found");
        }
        return transactionList;
    }

    public void accountValueUpdateSender(String accountNumberSender) throws AccountNotFoundException {
        Account senderAccount =  getAccountByAccountNumber(accountNumberSender).get(0);
        double senderAccountValue = senderAccount.getAccountValue();
        List<Transaction> senderTransactionsList =  senderAccount.getTransactions();
        for(Transaction transaction : senderTransactionsList) {
            senderAccountValue -= transaction.getTransactionValue();
        }
        updateAccountValue(senderAccount.getAccountId(), senderAccountValue);
    }

    public void accountValueUpdateReceiver(String accountNumberSender) throws AccountNotFoundException {
        Account senderAccount = getAccountByAccountNumber(accountNumberSender).get(0);
        List<Transaction> senderTransactionList = senderAccount.getTransactions();
        for (Transaction transaction : senderTransactionList) {
            String receiverAccountNumber = transaction.getTransactionReceiver();
            Account receiverAccount = getAccountByAccountNumber(receiverAccountNumber).get(0);
            double receiverAccountValue = receiverAccount.getAccountValue() ;
            receiverAccountValue += transaction.getTransactionValue();
            updateAccountValue(receiverAccount.getAccountId(), receiverAccountValue);
        }
    }
}
