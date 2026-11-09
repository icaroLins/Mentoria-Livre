package com.mentorialivre.mentoria_livre.service.mensagens;

import com.mentorialivre.mentoria_livre.dto.MensagemRequest;
import com.mentorialivre.mentoria_livre.dto.MensagemResponse;
import com.mentorialivre.mentoria_livre.model.mensagens.Mensagens;
import com.mentorialivre.mentoria_livre.model.mentoria.Mentoria;
import com.mentorialivre.mentoria_livre.model.user.User;
import com.mentorialivre.mentoria_livre.repository.mensagens.MensagensRepository;
import com.mentorialivre.mentoria_livre.repository.mentoria.MentoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MensagemService {

    private final MensagensRepository mensagemRepository;
    private final MentoriaRepository mentoriaRepository;
    private final ChatPermissionService chatPermissionService;

    public MensagemService(
            MensagensRepository mensagemRepository,
            MentoriaRepository mentoriaRepository,
            ChatPermissionService chatPermissionService
    ) {
        this.mensagemRepository = mensagemRepository;
        this.mentoriaRepository = mentoriaRepository;
        this.chatPermissionService = chatPermissionService;
    }

    @Transactional
    public MensagemResponse enviarMensagem(
            Long mentoriaId,
            User remetente,
            MensagemRequest request
    ) {

        chatPermissionService.validarEnvioDeMensagem(
                mentoriaId,
                remetente
        );

        Mentoria mentoria = mentoriaRepository.findById(mentoriaId)
                .orElseThrow(() -> new RuntimeException(
                        "Mentoria não encontrada!"
                ));

        Mensagens mensagem = new Mensagens();

        mensagem.setConteudo(request.conteudo().trim());
        mensagem.setMentoria(mentoria);
        mensagem.setUser(remetente);

        Mensagens mensagemSalva = mensagemRepository.save(mensagem);

        return converterParaResponse(mensagemSalva);
    }

    @Transactional(readOnly = true)
    public Page<MensagemResponse> buscarMensagens(
            Long mentoriaId,
            User usuario,
            Pageable pageable
    ) {

        chatPermissionService.validarAcessoAoChat(
                mentoriaId,
                usuario
        );

        return mensagemRepository
                .findByMentoria_IdOrderByDataEnvioDesc(
                        mentoriaId,
                        pageable
                )
                .map(this::converterParaResponse);
    }

    private MensagemResponse converterParaResponse(Mensagens mensagem) {

        return new MensagemResponse(
                mensagem.getId(),
                mensagem.getConteudo(),
                mensagem.getDataEnvio(),
                mensagem.getUser().getId(),
                mensagem.getMentoria().getId()
        );
    }
}
