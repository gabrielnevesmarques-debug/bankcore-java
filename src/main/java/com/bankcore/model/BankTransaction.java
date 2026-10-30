package com.bankcore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bank_transactions")
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected BankTransaction() {
    }

    public BankTransaction(Account account, TransactionType type, BigDecimal amount) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UUID getAccountId() { return account.getId(); }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
