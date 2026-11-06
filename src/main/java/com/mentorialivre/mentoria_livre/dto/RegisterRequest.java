package com.mentorialivre.mentoria_livre.dto;

import com.mentorialivre.mentoria_livre.model.user.Role;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String senha,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve estar no passado")
        LocalDate dataNascimento,

        @NotNull(message = "O perfil (role) é obrigatório: MENTOR ou MENTORADO")
        Role role,

        // Opcional: usado apenas quando role = MENTOR
        String especialidade,

        // Opcional: usado apenas quando role = MENTORADO
        String objetivo
) {
}
