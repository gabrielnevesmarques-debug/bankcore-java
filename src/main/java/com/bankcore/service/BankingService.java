package com.bankcore.service;

import com.bankcore.dto.*;
import com.bankcore.model.*;
import com.bankcore.repository.AccountRepository;
import com.bankcore.repository.BankTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class BankingService {
    private final AccountRepository accountRepository;
    private final BankTransactionRepository transactionRepository;

    public BankingService(AccountRepository accountRepository,
                          BankTransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        Account account = accountRepository.save(new Account(request.ownerName().trim()));
        return AccountResponse.from(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> listAccounts() {
        return accountRepository.findAll().stream().map(AccountResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(UUID accountId) {
        return AccountResponse.from(findAccount(accountId));
    }

    @Transactional
    public AccountResponse deposit(UUID accountId, BigDecimal amount) {
        validateAmount(amount);
        Account account = findAccount(accountId);
        account.deposit(amount);
        transactionRepository.save(new BankTransaction(account, TransactionType.DEPOSIT, amount));
        return AccountResponse.from(account);
    }

    @Transactional
    public AccountResponse withdraw(UUID accountId, BigDecimal amount) {
        validateAmount(amount);
        Account account = findAccount(accountId);
        account.withdraw(amount);
        transactionRepository.save(new BankTransaction(account, TransactionType.WITHDRAWAL, amount));
        return AccountResponse.from(account);
    }

    @Transactional
    public void transfer(UUID sourceId, TransferRequest request) {
        validateAmount(request.amount());
        if (sourceId.equals(request.destinationAccountId())) {
            throw new IllegalArgumentException("A conta de origem e destino devem ser diferentes.");
        }

        Account source = findAccount(sourceId);
        Account destination = findAccount(request.destinationAccountId());

        source.withdraw(request.amount());
        destination.deposit(request.amount());

        transactionRepository.save(new BankTransaction(source, TransactionType.TRANSFER_SENT, request.amount()));
        transactionRepository.save(new BankTransaction(destination, TransactionType.TRANSFER_RECEIVED, request.amount()));
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getHistory(UUID accountId) {
        findAccount(accountId);
        return transactionRepository.findByAccount_IdOrderByCreatedAtDesc(accountId)
                .stream().map(TransactionResponse::from).toList();
    }

    private Account findAccount(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
    }
}
