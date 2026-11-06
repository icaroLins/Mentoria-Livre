package com.mentorialivre.mentoria_livre.dto;

import com.mentorialivre.mentoria_livre.model.inscricao.Inscricao;

import java.util.UUID;

public record InscricaoResponse(
        Long id,
        String nome,
        String titulo_mentoria,
        String nomeMentor,
        UUID mentorado_id
) {
    public static InscricaoResponse from(Inscricao inscricao) {
       return new InscricaoResponse(
               inscricao.getId(),
               inscricao.getMentorado().getNome(),
               inscricao.getMentoria().getTitulo(),
               inscricao.getMentoria().getMentor().getNome(),
               inscricao.getMentorado().getId()
       );
    }
}
