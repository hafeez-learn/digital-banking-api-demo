package com.banking.transaction.service;

import com.banking.transaction.dto.*;
import com.banking.transaction.model.Transaction;
import com.banking.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setTransactionRef("TXN1234567890ABCD");
        testTransaction.setFromAccount("ACC123456789");
        testTransaction.setToAccount("ACC987654321");
        testTransaction.setAmount(BigDecimal.valueOf(100.00));
        testTransaction.setType(Transaction.TransactionType.TRANSFER);
        testTransaction.setStatus(Transaction.TransactionStatus.COMPLETED);
    }

    @Test
    void initiateTransfer_Success() {
        TransferRequest request = new TransferRequest();
        request.setFromAccount("ACC123456789");
        request.setToAccount("ACC987654321");
        request.setAmount(BigDecimal.valueOf(100.00));

        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        TransactionResponse response = transactionService.initiateTransfer(request);

        assertNotNull(response);
        assertEquals("ACC123456789", response.getFromAccount());
        assertEquals("ACC987654321", response.getToAccount());
        assertEquals(BigDecimal.valueOf(100.00), response.getAmount());
        assertEquals("TRANSFER", response.getType());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void getTransaction_Success() {
        when(transactionRepository.findByTransactionRef("TXN1234567890ABCD"))
            .thenReturn(Optional.of(testTransaction));

        TransactionResponse response = transactionService.getTransaction("TXN1234567890ABCD");

        assertNotNull(response);
        assertEquals("TXN1234567890ABCD", response.getTransactionRef());
        assertEquals(BigDecimal.valueOf(100.00), response.getAmount());
    }

    @Test
    void getTransaction_NotFound_ThrowsException() {
        when(transactionRepository.findByTransactionRef("TXN9999999999"))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
            () -> transactionService.getTransaction("TXN9999999999"));
    }

    @Test
    void getAccountTransactions_ReturnsList() {
        Transaction txn1 = new Transaction();
        txn1.setTransactionRef("TXN001");
        txn1.setFromAccount("ACC123456789");
        txn1.setToAccount("ACC111111111");
        txn1.setAmount(BigDecimal.valueOf(50.00));
        txn1.setType(Transaction.TransactionType.TRANSFER);
        txn1.setStatus(Transaction.TransactionStatus.COMPLETED);

        Transaction txn2 = new Transaction();
        txn2.setTransactionRef("TXN002");
        txn2.setFromAccount("ACC222222222");
        txn2.setToAccount("ACC123456789");
        txn2.setAmount(BigDecimal.valueOf(75.00));
        txn2.setType(Transaction.TransactionType.TRANSFER);
        txn2.setStatus(Transaction.TransactionStatus.COMPLETED);

        when(transactionRepository.findByFromAccountOrToAccountOrderByCreatedAtDesc("ACC123456789", "ACC123456789"))
            .thenReturn(Arrays.asList(txn1, txn2));

        List<TransactionResponse> transactions = transactionService.getAccountTransactions("ACC123456789");

        assertEquals(2, transactions.size());
        assertEquals("TXN001", transactions.get(0).getTransactionRef());
        assertEquals("TXN002", transactions.get(1).getTransactionRef());
    }

    @Test
    void getAccountTransactions_EmptyList() {
        when(transactionRepository.findByFromAccountOrToAccountOrderByCreatedAtDesc("ACC999999999", "ACC999999999"))
            .thenReturn(Arrays.asList());

        List<TransactionResponse> transactions = transactionService.getAccountTransactions("ACC999999999");

        assertTrue(transactions.isEmpty());
    }
}
