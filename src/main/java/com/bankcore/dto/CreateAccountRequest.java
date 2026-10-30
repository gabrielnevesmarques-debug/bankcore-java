package com.bankcore.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @NotBlank(message = "O nome do titular é obrigatório.")
        String ownerName
) {
}
