package com.banking.account.service;

import com.banking.account.dto.*;
import com.banking.account.model.Account;
import com.banking.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        if (accountRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new IllegalArgumentException("Account number already exists");
        }

        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setHolderName(request.getHolderName());
        account.setNric(request.getNric());
        account.setBalance(request.getInitialBalance() != null 
            ? BigDecimal.valueOf(request.getInitialBalance()) 
            : BigDecimal.ZERO);
        account.setStatus(Account.AccountStatus.ACTIVE);

        Account saved = accountRepository.save(account);
        return AccountResponse.fromEntity(saved);
    }

    public AccountResponse getAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));
        return AccountResponse.fromEntity(account);
    }

    public BigDecimal getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));
        return account.getBalance();
    }

    @Transactional
    public AccountResponse updateStatus(String accountNumber, String status) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));
        
        account.setStatus(Account.AccountStatus.valueOf(status.toUpperCase()));
        Account updated = accountRepository.save(account);
        return AccountResponse.fromEntity(updated);
    }

    @Transactional
    public void updateBalance(String accountNumber, BigDecimal newBalance) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}
