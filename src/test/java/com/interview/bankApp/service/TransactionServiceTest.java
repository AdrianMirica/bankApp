package com.interview.bankApp.service;

import com.interview.bankApp.exception.AccountNotFoundException;
import com.interview.bankApp.exception.TransactionNotFoundException;
import com.interview.bankApp.model.Transaction;
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
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    void testGetTransactionById() throws TransactionNotFoundException {
        Transaction transaction = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(1L)
                .transactionDate(LocalDateTime.now().minusMonths(1))
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(10d)
                .build();

        when(transactionRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(transaction));
        Transaction returnedTransaction = transactionService.getTransactionById(1L);

        assertEquals(1L, returnedTransaction.getTransactionId());
        assertEquals(new Double(10), returnedTransaction.getTransactionValue());
    }

    @Test
    void testGetAccountByIdThrowsException() {

        TransactionNotFoundException tnfe = assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(1L));
        String expectedMessage = "Transaction with ID = 1 was not found";
        String actualMessage = tnfe.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetAllTransactions() {
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

        when(transactionRepository.findAll()).thenReturn(transactionList);
        List<Transaction> returnedTransactionList = transactionService.getAllTransactions();

        assertEquals(returnedTransactionList, transactionList);
    }

    @Test
    void testDeleteATransactionByID() {
        transactionService.deleteTransactionById(1L);
        verify(transactionRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testCreateTransaction() {
        Transaction transaction = Transaction.builder()
                .transactionCurrency("RON")
                .transactionId(1L)
                .transactionDate(LocalDateTime.now().minusMonths(1))
                .transactionReceiver("5678")
                .transactionSender("1234")
                .transactionValue(10d)
                .build();

        transactionRepository.save(transaction);
        verify(transactionRepository, times(1)).save(any());
    }
}
