package com.interview.bankApp.service;

import com.interview.bankApp.model.Transaction;
import com.interview.bankApp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions(){
        List<Transaction> transactionList = new ArrayList<>();
        transactionRepository.findAll().forEach(transactionList::add);
        return transactionList;
    }

    public List<Transaction> getAllTransactionsForAnAccount(String accountNumber) {
        List<Transaction> transactionList = new ArrayList<>();
        transactionRepository.findAll().forEach(
                transaction -> {
                    if (transaction.getAccount().getAccountNumber().equalsIgnoreCase(accountNumber))
                        transactionList.add(transaction);
                }
        );
        return transactionList;
    }

    public List<Transaction> getAllTransactionsForAnAccountAfterDate(String accountNumber, Date desiredDate) {
        List<Transaction> transactionList = new ArrayList<>();
        transactionRepository.findAll().forEach(
                transaction -> {
                    if (transaction.getAccount().getAccountNumber().equalsIgnoreCase(accountNumber) && transaction.getTransactionDate().after(desiredDate))
                        transactionList.add(transaction);
                }
        );
        return transactionList;
    }

    public Transaction getTransactionById(int id) {
        if(transactionRepository.findById(id).isPresent()) {
            return transactionRepository.findById(id).get();
        } else
            return null; // exceptie aici
    }

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
