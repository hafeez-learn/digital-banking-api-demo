package com.banking.transaction.repository;

import com.banking.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionRef(String transactionRef);
    List<Transaction> findByFromAccountOrToAccountOrderByCreatedAtDesc(String fromAccount, String toAccount);
    List<Transaction> findByFromAccountOrderByCreatedAtDesc(String fromAccount);
}
