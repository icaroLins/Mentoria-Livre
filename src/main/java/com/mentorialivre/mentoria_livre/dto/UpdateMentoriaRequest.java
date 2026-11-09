package com.mentorialivre.mentoria_livre.dto;

import com.mentorialivre.mentoria_livre.model.mentoria.StatusMentoria;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/** Campos ausentes ou nulos preservam o valor atual. */
public record UpdateMentoriaRequest(
        @Pattern(regexp = "(?s).*\\S.*", message = "O titulo nao pode estar em branco")
        @Size(max = 255) String titulo,
        StatusMentoria statusMentoria,
        @Min(1) @Max(15) Integer quantidade,
        @Future(message = "A data deve ser no futuro") LocalDate data,
        @Pattern(regexp = "(?s).*\\S.*", message = "A descricao nao pode estar em branco")
        @Size(max = 1500) String descricao
) {
}
