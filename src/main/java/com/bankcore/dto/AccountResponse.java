package com.bankcore.dto;

import com.bankcore.model.Account;
import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(UUID id, String ownerName, BigDecimal balance) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getId(), account.getOwnerName(), account.getBalance());
    }
}
