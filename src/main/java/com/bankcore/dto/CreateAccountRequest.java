package com.bankcore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
        @NotBlank(message = "O nome do titular é obrigatório.")
        @Size(max = 100, message = "O nome do titular deve ter no máximo 100 caracteres.")
        String ownerName
) {
}