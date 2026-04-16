package com.banking.account.service;

import com.banking.account.dto.*;
import com.banking.account.model.Account;
import com.banking.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAccountNumber("ACC123456789");
        testAccount.setHolderName("John Doe");
        testAccount.setNric("S123456789");
        testAccount.setBalance(BigDecimal.valueOf(1000.00));
        testAccount.setStatus(Account.AccountStatus.ACTIVE);
        testAccount.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createAccount_Success() {
        AccountRequest request = new AccountRequest();
        request.setAccountNumber("ACC123456789");
        request.setHolderName("John Doe");
        request.setNric("S123456789");
        request.setInitialBalance(1000.00);

        when(accountRepository.existsByAccountNumber("ACC123456789")).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        AccountResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals("ACC123456789", response.getAccountNumber());
        assertEquals("John Doe", response.getHolderName());
        assertEquals(BigDecimal.valueOf(1000.00), response.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_DuplicateAccountNumber_ThrowsException() {
        AccountRequest request = new AccountRequest();
        request.setAccountNumber("ACC123456789");

        when(accountRepository.existsByAccountNumber("ACC123456789")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(request));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void getAccount_Success() {
        when(accountRepository.findByAccountNumber("ACC123456789")).thenReturn(Optional.of(testAccount));

        AccountResponse response = accountService.getAccount("ACC123456789");

        assertNotNull(response);
        assertEquals("ACC123456789", response.getAccountNumber());
        assertEquals("John Doe", response.getHolderName());
    }

    @Test
    void getAccount_NotFound_ThrowsException() {
        when(accountRepository.findByAccountNumber("ACC999999999")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> accountService.getAccount("ACC999999999"));
    }

    @Test
    void getBalance_Success() {
        when(accountRepository.findByAccountNumber("ACC123456789")).thenReturn(Optional.of(testAccount));

        BigDecimal balance = accountService.getBalance("ACC123456789");

        assertEquals(BigDecimal.valueOf(1000.00), balance);
    }

    @Test
    void updateStatus_Success() {
        when(accountRepository.findByAccountNumber("ACC123456789")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        AccountResponse response = accountService.updateStatus("ACC123456789", "FROZEN");

        assertNotNull(response);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void updateBalance_Success() {
        when(accountRepository.findByAccountNumber("ACC123456789")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        accountService.updateBalance("ACC123456789", BigDecimal.valueOf(500.00));

        verify(accountRepository, times(1)).save(any(Account.class));
    }
}
