package com.interview.bankApp.service;

import com.interview.bankApp.exception.AccountNotFoundException;
import com.interview.bankApp.exception.InsufficientAmountException;
import com.interview.bankApp.exception.InvalidInputException;
import com.interview.bankApp.model.Account;
import com.interview.bankApp.model.Transaction;
import com.interview.bankApp.repository.AccountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;

/**
 * This is the {@code @Service} class for the account
 * @author Adrian
 * @version 1.0
 */
@Service
public class AccountService {

    private final static Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    AccountRepository accountRepository;

    /**
     * This method returns all the accounts from the {@code accountRepository}
     * @return {@code List<Account>}
     */
    public List<Account> getAllAccounts() {
        List<Account> accountList = new ArrayList<>();
        accountRepository.findAll().forEach(accountList::add);
        return accountList;
    }

    /**
     * This method returns a specific account from the {@code accountRepository}
     * @param id represents the account id
     * @return {@code Account}
     * @throws AccountNotFoundException - if the {@code id} is not found in the database this error is thrown
     */
    public Account getAccountById(long id) throws AccountNotFoundException {
        if(accountRepository.findById(id).isPresent())
            return accountRepository.findById(id).get();
        else
            throw new AccountNotFoundException("Account with ID = " + id + " was not found");
    }

    /**
     * This method returns a specific account from the {@code accountRepository} using the {@code accNumber}
     * @param accNumber - parameter used to retrieve the account
     * @return {@code Account}
     */
    public Account getAccountByAccountNumber(String accNumber) {
        return accountRepository.findByAccountNumber(accNumber);
    }

    /**
     * This method removes a specific account that is identified by id
     * @param id - long variable that represents the account id
     */
    public void deleteAccountById(long id){
        accountRepository.deleteById(id);
    }

    /**
     * This method creates a new account. Fields are validated using {@method allowOnlyLettersAndDigits} so that invalid input cannot be entered.
     * @param account - new account information
     * @throws InvalidInputException - if the validation are not meet this error is thrown
     */
    public void createAccount(Account account) throws InvalidInputException {
        if(allowOnlyLettersAndDigits(account.getAccountNumber()) && account.getAccountValue() >= 0)
            accountRepository.save(account);
        else throw new InvalidInputException("You have entered some invalid information for this account. Please check accountNumber or accountValue. AccountNumber = "
                + account.getAccountNumber() + " ; AccountValue = " +account.getAccountValue());
    }

    public void updateAccountStatus(long id, String status) throws AccountNotFoundException {
        if(accountRepository.findById(id).isPresent()) {
            accountRepository.findById(id).get().setAccountStatus(status);
            accountRepository.save(accountRepository.findById(id).get());
        }
        else
            throw new AccountNotFoundException("Account with ID = " + id + " was not found");
    }

    public void updateAccountValue(long id, double newAccountValue) throws AccountNotFoundException {
        if(accountRepository.findById(id).isPresent()) {
            accountRepository.findById(id).get().setAccountValue(newAccountValue);
            accountRepository.save(accountRepository.findById(id).get());
            logger.info("Account with ID = " + id + " was updated with accountValue =" + newAccountValue);
        }
        else {
            throw new AccountNotFoundException("Account with ID = " + id + " was not found");
        }
    }

    public List<Transaction> getAllTransactionsForAnAccount(long id) throws AccountNotFoundException {
         return getAccountById(id).getTransactions();
    }

    public List<Transaction> getAllTransactionsForAnAccountAfterDate(long id, String desiredDateString) throws AccountNotFoundException, InvalidInputException {
        List<Transaction> transactionList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate desiredDateConverted = LocalDate.parse(desiredDateString, formatter);
        logger.info("desiredDateConverted = " + desiredDateString);
        if(desiredDateConverted.isAfter(ChronoLocalDate.from(LocalDateTime.now()))) {
            throw new InvalidInputException("You have have entered a date in the future" + desiredDateString + " , please retry with a correct date");
        }
        try {
            getAccountById(id).getTransactions().forEach(
                    transaction -> {if(transaction.getTransactionDate().toLocalDate().isAfter(desiredDateConverted))
                    transactionList.add(transaction);}
            );
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException("Account with ID = " + id + " was not found");
        }
        return transactionList;
    }

    public List<Transaction> getAllTransactionsForAnAccountForToday(long id) throws AccountNotFoundException {
        List<Transaction> transactionList = new ArrayList<>();
        LocalDate yesterday = LocalDateTime.now().minusDays(1).toLocalDate();
        logger.info("Today= " + yesterday);
        try {
            getAccountById(id).getTransactions().forEach(
                    transaction -> {if(transaction.getTransactionDate().toLocalDate().isAfter(yesterday))
                    transactionList.add(transaction);}
            );
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException("Account with ID = " + id + " was not found");
        }
        return transactionList;
    }

    public List<Transaction> getAllTransactionsForAnAccountForYesterday(long id) throws AccountNotFoundException {
        List<Transaction> transactionList = new ArrayList<>();
        LocalDate dayBeforeYesterday = LocalDateTime.now().minusDays(2).toLocalDate();
        try {
            getAccountById(id).getTransactions().forEach(
                    transaction -> {if(transaction.getTransactionDate().toLocalDate().isAfter(dayBeforeYesterday))
                    transactionList.add(transaction);}
            );
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException("Account with ID = " + id + " was not found");
        }
        return transactionList;
    }

    public List<Transaction> getAllTransactionsForAnAccountForLastHours(long id, long hours) throws AccountNotFoundException, InvalidInputException {
        List<Transaction> transactionList = new ArrayList<>();
        if(hours <= 0)
            throw new InvalidInputException("You have entered a invalid parameter, " + hours + " . Hours parameter should be always positive");
        LocalDateTime time = LocalDateTime.now().minusHours(hours);
        try {
            getAccountById(id).getTransactions().forEach(
                    transaction -> {if(transaction.getTransactionDate().isAfter(time))
                    transactionList.add(transaction);}
            );
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException("Account with ID = " + id + " was not found");
        }
        return transactionList;
    }

    /**
     * This method updates the {@code accountValue} for the sender
     * @param accountNumberSender - parameter used to identify the sender account
     * @throws AccountNotFoundException -  if the account is not found, error is thrown
     */
    public void accountValueUpdateSender(String accountNumberSender) throws AccountNotFoundException {
        Account senderAccount =  getAccountByAccountNumber(accountNumberSender);
        double senderAccountValue = senderAccount.getAccountValue();
        List<Transaction> senderTransactionsList =  senderAccount.getTransactions();
        for(Transaction transaction : senderTransactionsList) {
            if(transaction.getTransactionValue() <= senderAccountValue)
                senderAccountValue -= transaction.getTransactionValue();
            else
                throw new InsufficientAmountException("The account with account number = " + senderAccount.getAccountNumber() + " has insufficient funds");
        }
        updateAccountValue(senderAccount.getAccountId(), senderAccountValue);
    }

    /**
     * This method updates the {@code accountValue} for the receiver
     * @param accountNumberSender- parameter used to identify the sender account
     * @throws AccountNotFoundException -  if the account is not found, error is thrown
     */
    public void accountValueUpdateReceiver(String accountNumberSender) throws AccountNotFoundException {
        Account senderAccount = getAccountByAccountNumber(accountNumberSender);
        List<Transaction> senderTransactionList = senderAccount.getTransactions();
        for (Transaction transaction : senderTransactionList) {
            String receiverAccountNumber = transaction.getTransactionReceiver();
            Account receiverAccount = getAccountByAccountNumber(receiverAccountNumber);
            double receiverAccountValue = receiverAccount.getAccountValue() ;
            receiverAccountValue += transaction.getTransactionValue();
            updateAccountValue(receiverAccount.getAccountId(), receiverAccountValue);
        }
    }

    /**
     * This method checks if the text entered has only letters and digits
     * @param textToBeVerified
     * @return {@code boolean}
     */
    private boolean allowOnlyLettersAndDigits(String textToBeVerified) {
        return textToBeVerified.matches("\\w+");
    }
}
