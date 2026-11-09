package com.mentorialivre.mentoria_livre.service.mensagens;

import com.mentorialivre.mentoria_livre.model.inscricao.StatusInscricao;
import com.mentorialivre.mentoria_livre.model.mentoria.Mentoria;
import com.mentorialivre.mentoria_livre.model.mentoria.StatusMentoria;
import com.mentorialivre.mentoria_livre.model.user.User;
import com.mentorialivre.mentoria_livre.repository.inscricao.InscricaoRepository;
import com.mentorialivre.mentoria_livre.repository.mentoria.MentoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatPermissionService {

    private final MentoriaRepository mentoriaRepository;
    private final InscricaoRepository inscricaoRepository;

    public ChatPermissionService(
            MentoriaRepository mentoriaRepository,
            InscricaoRepository inscricaoRepository
    ) {
        this.mentoriaRepository = mentoriaRepository;
        this.inscricaoRepository = inscricaoRepository;
    }

    @Transactional(readOnly = true)
    public void validarAcessoAoChat(Long mentoriaId, User usuario) {

        Mentoria mentoria = buscarMentoria(mentoriaId);

        boolean ehMentorResponsavel =
                mentoria.getMentor().getId().equals(usuario.getId());

        boolean possuiInscricaoAtiva =
                inscricaoRepository
                        .existsByMentorado_IdAndMentoria_IdAndStatusInscricao(
                                usuario.getId(),
                                mentoriaId,
                                StatusInscricao.ATIVA
                        );

        if (!ehMentorResponsavel && !possuiInscricaoAtiva) {
            throw new RuntimeException(
                    "Você não possui autorização para acessar este chat!"
            );
        }
    }

    @Transactional(readOnly = true)
    public void validarEnvioDeMensagem(Long mentoriaId, User usuario) {

        Mentoria mentoria = buscarMentoria(mentoriaId);

        if (mentoria.getStatus() != StatusMentoria.EM_ANDAMENTO) {
            throw new RuntimeException(
                    "Não é possível enviar mensagens nesta mentoria!"
            );
        }

        validarAcessoAoChat(mentoriaId, usuario);
    }

    private Mentoria buscarMentoria(Long mentoriaId) {

        return mentoriaRepository.findById(mentoriaId)
                .orElseThrow(() -> new RuntimeException(
                        "Mentoria não encontrada!"
                ));
    }
}