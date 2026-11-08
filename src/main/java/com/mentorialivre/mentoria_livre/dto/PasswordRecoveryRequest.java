package com.mentorialivre.mentoria_livre.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordRecoveryRequest(
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    String email
) {
}
