package com.mentorialivre.mentoria_livre.dto;

import com.mentorialivre.mentoria_livre.model.mentoria.StatusMentoria;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record GenerateMentoriaRequest(
        @NotBlank(message = "O titulo é obrigatorio!")
        String titulo,

        @NotNull(message = "O status é obrigatorio: ABERTO, EM_ANDAMENTO e ENCERRADA")
        StatusMentoria statusMentoria,

        @NotNull(message = "A quantidade é obrigatoria!")
        @Min(value = 1, message = "A quantidade mínima é 1")
        @Max(value = 15, message = "A quantidade máxima é 15")
        Integer quantidade,

        @NotNull(message = "A data é obrigatoria!")
        @Future(message = "A data deve ser no futuro!")
        LocalDate data,

        @NotBlank(message = "A descrição é obrigatoria!")
        String descricao
) {
}
