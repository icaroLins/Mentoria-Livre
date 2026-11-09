package com.mentorialivre.mentoria_livre.dto;

import java.time.Instant;
import java.util.UUID;

public record MensagemResponse(
        UUID id,
        String conteudo,
        Instant dataEnvio,
        UUID remetenteId,
        Long mentoriaId
) {}