package com.interview.bankApp.service;

import com.interview.bankApp.exception.TransactionNotFoundException;
import com.interview.bankApp.model.Transaction;
import com.interview.bankApp.repository.AccountRepository;
import com.interview.bankApp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    public List<Transaction> getAllTransactions(){
        List<Transaction> transactionList = new ArrayList<>();
        transactionRepository.findAll().forEach(transactionList::add);
        return transactionList;
    }

    public Transaction getTransactionById(int id) throws TransactionNotFoundException {
        if(transactionRepository.findById(id).isPresent()) {
            return transactionRepository.findById(id).get();
        } else
            throw new TransactionNotFoundException("Transaction not found");
    }

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
        accountRepository.findAll().forEach(
                account -> {
                    if (account.getAccountNumber().equals(transaction.getTransactionSender())) {
                        account.getTransactions().add(transaction);
                    }
                }
        );
        accountRepository.findAll().forEach(
                account -> {
                    if (account.getAccountNumber().equals(transaction.getTransactionReceiver())) {
                        account.getTransactions().add(transaction);
                    }
                }
        );
    }

    public void deleteTransactionById(int id) { transactionRepository.deleteById(id);}
}
