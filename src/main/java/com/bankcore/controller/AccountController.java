package com.bankcore.controller;

import com.bankcore.dto.*;
import com.bankcore.service.BankingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final BankingService bankingService;

    public AccountController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(@Valid @RequestBody CreateAccountRequest request) {
        return bankingService.createAccount(request);
    }

    @GetMapping
    public List<AccountResponse> list() {
        return bankingService.listAccounts();
    }

    @GetMapping("/{id}")
    public AccountResponse get(@PathVariable UUID id) {
        return bankingService.getAccount(id);
    }

    @PostMapping("/{id}/deposit")
    public AccountResponse deposit(@PathVariable UUID id, @Valid @RequestBody AmountRequest request) {
        return bankingService.deposit(id, request.amount());
    }

    @PostMapping("/{id}/withdraw")
    public AccountResponse withdraw(@PathVariable UUID id, @Valid @RequestBody AmountRequest request) {
        return bankingService.withdraw(id, request.amount());
    }

    @PostMapping("/{id}/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transfer(@PathVariable UUID id, @Valid @RequestBody TransferRequest request) {
        bankingService.transfer(id, request);
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionResponse> history(@PathVariable UUID id) {
        return bankingService.getHistory(id);
    }
}
