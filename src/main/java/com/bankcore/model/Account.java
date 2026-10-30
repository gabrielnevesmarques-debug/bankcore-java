package com.bankcore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {
    private static final BigDecimal MAX_BALANCE = new BigDecimal("99999999999999999.99");

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String ownerName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    protected Account() {
    }

    public Account(String ownerName) {
        this.ownerName = ownerName;
        this.balance = BigDecimal.ZERO;
    }

    public void deposit(BigDecimal amount) {
        BigDecimal newBalance = balance.add(amount);
        if (newBalance.compareTo(MAX_BALANCE) > 0) {
            throw new IllegalArgumentException("O saldo ultrapassa o limite permitido.");
        }
        balance = newBalance;
    }

    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        balance = balance.subtract(amount);
    }

    public UUID getId() { return id; }
    public String getOwnerName() { return ownerName; }
    public BigDecimal getBalance() { return balance; }
}