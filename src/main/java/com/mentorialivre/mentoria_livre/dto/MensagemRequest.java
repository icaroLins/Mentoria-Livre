package com.mentorialivre.mentoria_livre.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MensagemRequest(

        @NotBlank(message = "O conteúdo da mensagem é obrigatório!")
        @Size(max = 2000, message = "A mensagem deve ter no máximo 2000 caracteres!")
        String conteudo

) {}
