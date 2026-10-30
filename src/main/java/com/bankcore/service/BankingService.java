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
        Account account = findAccountForUpdate(accountId);
        account.deposit(amount);
        transactionRepository.save(new BankTransaction(account, TransactionType.DEPOSIT, amount));
        return AccountResponse.from(account);
    }

    @Transactional
    public AccountResponse withdraw(UUID accountId, BigDecimal amount) {
        validateAmount(amount);
        Account account = findAccountForUpdate(accountId);
        account.withdraw(amount);
        transactionRepository.save(new BankTransaction(account, TransactionType.WITHDRAWAL, amount));
        return AccountResponse.from(account);
    }

    @Transactional
    public void transfer(UUID sourceId, TransferRequest request) {
        validateAmount(request.amount());
        UUID destinationId = request.destinationAccountId();

        if (sourceId.equals(destinationId)) {
            throw new IllegalArgumentException("A conta de origem e destino devem ser diferentes.");
        }

        // Bloqueia as contas sempre na mesma ordem para reduzir o risco de deadlock.
        UUID firstId = sourceId.compareTo(destinationId) < 0 ? sourceId : destinationId;
        UUID secondId = sourceId.compareTo(destinationId) < 0 ? destinationId : sourceId;

        Account first = findAccountForUpdate(firstId);
        Account second = findAccountForUpdate(secondId);

        Account source = first.getId().equals(sourceId) ? first : second;
        Account destination = first.getId().equals(destinationId) ? first : second;

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

    private Account findAccountForUpdate(UUID id) {
        return accountRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("O valor deve ter no máximo duas casas decimais.");
        }
        if (amount.precision() - amount.scale() > 17) {
            throw new IllegalArgumentException("O valor informado é muito alto.");
        }
    }
}