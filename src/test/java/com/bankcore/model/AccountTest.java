package com.bankcore.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    @Test
    void newAccountStartsWithZeroBalance() {
        Account account = new Account("Ana Silva");

        assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    void depositIncreasesBalance() {
        Account account = new Account("Ana Silva");

        account.deposit(new BigDecimal("150.00"));

        assertEquals(0, account.getBalance().compareTo(new BigDecimal("150.00")));
    }

    @Test
    void withdrawalDecreasesBalance() {
        Account account = new Account("Ana Silva");
        account.deposit(new BigDecimal("150.00"));

        account.withdraw(new BigDecimal("40.00"));

        assertEquals(0, account.getBalance().compareTo(new BigDecimal("110.00")));
    }

    @Test
    void withdrawalCannotMakeBalanceNegative() {
        Account account = new Account("Ana Silva");

        assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(new BigDecimal("10.00")));
    }

    @Test
    void depositCannotExceedDatabasePrecision() {
        Account account = new Account("Ana Silva");
        account.deposit(new BigDecimal("99999999999999999.99"));

        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(new BigDecimal("0.01")));
    }
}