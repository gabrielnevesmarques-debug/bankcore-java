package com.bankcore.repository;

import com.bankcore.model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, UUID> {
    List<BankTransaction> findByAccount_IdOrderByCreatedAtDesc(UUID accountId);
}
