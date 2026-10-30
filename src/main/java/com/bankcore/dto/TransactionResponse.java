package com.bankcore.dto;

import com.bankcore.model.BankTransaction;
import com.bankcore.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id, UUID accountId, TransactionType type, BigDecimal amount, LocalDateTime createdAt
) {
    public static TransactionResponse from(BankTransaction transaction) {
        return new TransactionResponse(
                transaction.getId(), transaction.getAccountId(), transaction.getType(),
                transaction.getAmount(), transaction.getCreatedAt()
        );
    }
}
