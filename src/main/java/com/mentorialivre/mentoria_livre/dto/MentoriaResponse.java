package com.mentorialivre.mentoria_livre.dto;

import com.mentorialivre.mentoria_livre.model.mentoria.Mentoria;
import com.mentorialivre.mentoria_livre.model.mentoria.StatusMentoria;

import java.time.LocalDate;

public record MentoriaResponse(
        Long id,
        String titulo,
        StatusMentoria statusMentoria,
        String nome,
        Integer quantidade,
        LocalDate data,
        String descricao
) {
    public static MentoriaResponse from(Mentoria mentoria) {
        return new MentoriaResponse(
                mentoria.getId(),
                mentoria.getTitulo(),
                mentoria.getStatus(),
                mentoria.getMentor().getNome(),
                mentoria.getQuantidade(),
                mentoria.getData(),
                mentoria.getDescricao()
        );
    }
}
