package com.interview.bankApp.service;

import com.interview.bankApp.exception.AccountNotFoundException;
import com.interview.bankApp.exception.InsufficientAmountException;
import com.interview.bankApp.exception.InvalidInputException;
import com.interview.bankApp.model.Account;
import com.interview.bankApp.model.Transaction;
import com.interview.bankApp.repository.AccountRepository;
import com.interview.bankApp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;


    @Test
    void testGetAccountById() throws AccountNotFoundException {
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(123d)
                .build();

        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(acc));
        Account returnedAccount = accountService.getAccountById(1L);
        assertEquals(1L, returnedAccount.getAccountId());
        assertEquals("1234", returnedAccount.getAccountNumber());
        assertEquals("RON", returnedAccount.getAccountCurrency());
        assertEquals(new Double(123), returnedAccount.getAccountValue());
    }

    @Test
    void testGetAccountByIdThrowsException() {

        AccountNotFoundException anfe = assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(1L));
        String expectedMessage = "Account with ID = 1 was not found";
        String actualMessage = anfe.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetAllAccounts() {
        Account acc1 = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(123d)
                .build();

        Account acc2 = Account.builder()
                .accountId(2L)
                .accountNumber("5678")
                .accountCurrency("EUR")
                .accountStatus("OPEN")
                .accountValue(100d)
                .build();

        List<Account> accountList = new ArrayList<>();
        accountList.add(acc1);
        accountList.add(acc2);

        when(accountRepository.findAll()).thenReturn(accountList);
        List<Account> returnedAccountsList = accountService.getAllAccounts();
        assertEquals(accountList, returnedAccountsList);
    }

    @Test
    void testGetAccountByAccountNumber() {
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(123d)
                .build();

        when(accountRepository.findByAccountNumber("1234")).thenReturn(acc);
        Account returnedAccount = accountService.getAccountByAccountNumber("1234");
        assertEquals("1234", returnedAccount.getAccountNumber());
        assertEquals(1L, returnedAccount.getAccountId());
    }

    @Test
    void testDeleteAccountById() {

        accountService.deleteAccountById(1L);
        verify(accountRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testCreateAccount() throws InvalidInputException {
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(123d)
                .build();

        accountService.createAccount(acc);
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void testCreateAccountThrowsInvalidInputException() {
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(-1d)
                .build();

        InvalidInputException iie = assertThrows(InvalidInputException.class, () -> accountService.createAccount(acc));
        String expectedMessage = "You have entered some invalid information for this account. Please check accountNumber or accountValue. AccountNumber = "
                +acc.getAccountNumber() +  " ; AccountValue = " +acc.getAccountValue();
        String actualMessage = iie.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testUpdateAccountStatus() throws AccountNotFoundException {
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(123d)
                .build();

        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(acc));
        accountService.updateAccountStatus(1L, "CLOSED");

        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void testUpdateAccountStatusThrowsANFE() {
        AccountNotFoundException anfe = assertThrows(AccountNotFoundException.class, () -> accountService.updateAccountStatus(1L, "CLOSED"));
        String expectedMessage = "Account with ID = 1 was not found";
        String actualMessage = anfe.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testUpdateAccountValue() throws AccountNotFoundException {
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(123d)
                .build();

        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(acc));
        accountService.updateAccountValue(1L, 500);

        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void testUpdateAccountValueThrowsANFE() {
        AccountNotFoundException anfe = assertThrows(AccountNotFoundException.class, () -> accountService.updateAccountValue(1L, 500));
        String expectedMessage = "Account with ID = 1 was not found";
        String actualMessage = anfe.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetAllTransactionFromAnAcount() throws AccountNotFoundException {
        Transaction transaction = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(1L)
                .transactionDate(LocalDateTime.now())
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(10d)
                .build();
        Transaction transaction2 = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(2L)
                .transactionDate(LocalDateTime.now())
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(100d)
                .build();
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        transactionList.add(transaction2);
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(123d)
                .transactions(transactionList)
                .build();

        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(acc));
        List<Transaction> returnedTransactionLIst = accountService.getAllTransactionsForAnAccount(1L);

        assertEquals(transactionList, returnedTransactionLIst);
    }

    @Test
    void testGetAllTransactionsFromAnAccountAfterDate() throws AccountNotFoundException, InvalidInputException {
        Transaction transaction = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(1L)
                .transactionDate(LocalDateTime.now().minusMonths(1))
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(10d)
                .build();
        Transaction transaction2 = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(2L)
                .transactionDate(LocalDateTime.now())
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(100d)
                .build();
        List<Transaction> transactionList = new ArrayList<>();
        List<Transaction> validTransactionList = new ArrayList<>();
        transactionList.add(transaction);
        transactionList.add(transaction2);
        validTransactionList.add(transaction2);
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(123d)
                .transactions(transactionList)
                .build();
        String desiredDate = "2020-11-01";
        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(acc));
        List<Transaction> returnedTransactionLIst = accountService.getAllTransactionsForAnAccountAfterDate(1L, desiredDate);

        assertEquals(validTransactionList, returnedTransactionLIst);
    }

    @Test
    void testGetAllTransactionFromAnAcountAccountNotFound() {
        AccountNotFoundException anfe = assertThrows(AccountNotFoundException.class, () -> accountService.getAllTransactionsForAnAccount(1L));
        String expectedMessage = "Account with ID = 1 was not found";
        String actualMessage = anfe.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetAllTransactionsFromAnAccountAfterDateAccountNotFound() {
        String desiredDate = "2020-11-01";
        AccountNotFoundException anfe = assertThrows(AccountNotFoundException.class, () -> accountService.getAllTransactionsForAnAccountAfterDate(1L, desiredDate));
        String expectedMessage = "Account with ID = 1 was not found";
        String actualMessage = anfe.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetAllTransactionsFromAnAccountAfterDateInvalidDate() {
        String desiredDate = LocalDateTime.now().plusYears(1).toLocalDate().toString();
        InvalidInputException iie = assertThrows(InvalidInputException.class, () -> accountService.getAllTransactionsForAnAccountAfterDate(1L, desiredDate));
        String expectedMessage = "You have have entered a date in the future" + desiredDate + " , please retry with a correct date";
        String actualMessage = iie.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetAllTransactionsFromAnAccountForToday() throws AccountNotFoundException {
        Transaction transaction = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(1L)
                .transactionDate(LocalDateTime.now().minusMonths(1))
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(10d)
                .build();
        Transaction transaction2 = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(2L)
                .transactionDate(LocalDateTime.now())
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(100d)
                .build();
        List<Transaction> transactionList = new ArrayList<>();
        List<Transaction> validTransactionList = new ArrayList<>();
        transactionList.add(transaction);
        transactionList.add(transaction2);
        validTransactionList.add(transaction2);
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(123d)
                .transactions(transactionList)
                .build();
        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(acc));
        List<Transaction> returnedTransactionLIst = accountService.getAllTransactionsForAnAccountForToday(1L);

        assertEquals(validTransactionList, returnedTransactionLIst);
    }

    @Test
    void testGetAllTransactionsFromAnAccountForTodayAccountNotFound() {
        AccountNotFoundException anfe = assertThrows(AccountNotFoundException.class, () -> accountService.getAllTransactionsForAnAccountForToday(1L));
        String expectedMessage = "Account with ID = 1 was not found";
        String actualMessage = anfe.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetAllTransactionsFromAnAccountForYesterday() throws AccountNotFoundException {
        Transaction transaction = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(1L)
                .transactionDate(LocalDateTime.now().minusMonths(1))
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(10d)
                .build();
        Transaction transaction2 = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(2L)
                .transactionDate(LocalDateTime.now().minusDays(1))
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(100d)
                .build();
        List<Transaction> transactionList = new ArrayList<>();
        List<Transaction> validTransactionList = new ArrayList<>();
        transactionList.add(transaction);
        transactionList.add(transaction2);
        validTransactionList.add(transaction2);
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(123d)
                .transactions(transactionList)
                .build();
        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(acc));
        List<Transaction> returnedTransactionLIst = accountService.getAllTransactionsForAnAccountForYesterday(1L);

        assertEquals(validTransactionList, returnedTransactionLIst);
    }

    @Test
    void testGetAllTransactionsFromAnAccountForYesterdayAccountNotFound() {
        AccountNotFoundException anfe = assertThrows(AccountNotFoundException.class, () -> accountService.getAllTransactionsForAnAccountForYesterday(1L));
        String expectedMessage = "Account with ID = 1 was not found";
        String actualMessage = anfe.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetAllTransactionsForAnAccountForLastHours() throws AccountNotFoundException, InvalidInputException {
        Transaction transaction = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(1L)
                .transactionDate(LocalDateTime.now().minusMonths(1))
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(10d)
                .build();
        Transaction transaction2 = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(2L)
                .transactionDate(LocalDateTime.now())
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(100d)
                .build();
        List<Transaction> transactionList = new ArrayList<>();
        List<Transaction> validTransactionList = new ArrayList<>();
        transactionList.add(transaction);
        transactionList.add(transaction2);
        validTransactionList.add(transaction2);
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(123d)
                .transactions(transactionList)
                .build();
        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(acc));
        List<Transaction> returnedTransactionLIst = accountService.getAllTransactionsForAnAccountForLastHours(1L, 12L);

        assertEquals(validTransactionList, returnedTransactionLIst);
    }

    @Test
    void testGetAllTransactionsForAnAccountForLastHoursAccountNotFound() {
        AccountNotFoundException anfe = assertThrows(AccountNotFoundException.class, () -> accountService.getAllTransactionsForAnAccountForLastHours(1L, 12L));
        String expectedMessage = "Account with ID = 1 was not found";
        String actualMessage = anfe.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetAllTransactionsForAnAccountForLastHoursDateInvalidDate() {
        long hours = -12;
        InvalidInputException iie = assertThrows(InvalidInputException.class, () -> accountService.getAllTransactionsForAnAccountForLastHours(1L, hours));
        String expectedMessage = "You have entered a invalid parameter, " + hours + " . Hours parameter should be always positive";
        String actualMessage = iie.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testAccountValueUpdateSender() throws AccountNotFoundException {
        Transaction transaction = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(1L)
                .transactionDate(LocalDateTime.now().minusMonths(1))
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(10d)
                .build();
        Transaction transaction2 = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(2L)
                .transactionDate(LocalDateTime.now())
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(100d)
                .build();
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        transactionList.add(transaction2);
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(500d)
                .transactions(transactionList)
                .build();

        when(accountRepository.findByAccountNumber("1234")).thenReturn(acc);
        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(acc));
        Account returnedAccount = accountService.getAccountByAccountNumber("1234");

        accountService.accountValueUpdateSender(returnedAccount.getAccountNumber());

        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void testAccountValueUpdateSenderThrowsInsufficientAmountException() {
        Transaction transaction = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(1L)
                .transactionDate(LocalDateTime.now().minusMonths(1))
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(10d)
                .build();
        Transaction transaction2 = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(2L)
                .transactionDate(LocalDateTime.now())
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(100d)
                .build();
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        transactionList.add(transaction2);
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("1234")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(10d)
                .transactions(transactionList)
                .build();

        when(accountRepository.findByAccountNumber("1234")).thenReturn(acc);
        Account returnedAccount = accountService.getAccountByAccountNumber("1234");

        InsufficientAmountException iae = assertThrows(InsufficientAmountException.class, () -> accountService.accountValueUpdateSender(returnedAccount.getAccountNumber()));
        String expectedMessage = "The account with account number = " + returnedAccount.getAccountNumber() + " has insufficient funds";
        String actualMessage = iae.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testAccountValueUpdateReceiver() throws AccountNotFoundException {
        Transaction transaction = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(1L)
                .transactionDate(LocalDateTime.now().minusMonths(1))
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(10d)
                .build();
        Transaction transaction2 = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(2L)
                .transactionDate(LocalDateTime.now())
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(100d)
                .build();
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        transactionList.add(transaction2);
        Account acc = Account.builder()
                .accountId(1L)
                .accountNumber("5678")
                .accountCurrency("RON")
                .accountStatus("OPEN")
                .accountValue(0d)
                .transactions(transactionList)
                .build();

        when(accountRepository.findByAccountNumber("5678")).thenReturn(acc);
        when(accountRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(acc));
        Account returnedAccount = accountService.getAccountByAccountNumber("5678");

        accountService.accountValueUpdateReceiver(returnedAccount.getAccountNumber());

        verify(accountRepository, times(2)).save(any());
    }
}
