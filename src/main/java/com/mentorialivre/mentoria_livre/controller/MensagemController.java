package com.mentorialivre.mentoria_livre.controller;

import com.mentorialivre.mentoria_livre.dto.MensagemRequest;
import com.mentorialivre.mentoria_livre.dto.MensagemResponse;
import com.mentorialivre.mentoria_livre.model.user.User;
import com.mentorialivre.mentoria_livre.service.mensagens.MensagemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mentorias/{mentoriaId}/mensagens")
public class MensagemController {

    private final MensagemService mensagemService;
    private final SimpMessagingTemplate messagingTemplate;

    public MensagemController(
            MensagemService mensagemService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.mensagemService = mensagemService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    public ResponseEntity<Page<MensagemResponse>> buscarMensagens(
            @PathVariable Long mentoriaId,
            @AuthenticationPrincipal User usuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {

        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Parâmetros de paginação inválidos!"
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "dataEnvio")
        );

        Page<MensagemResponse> mensagens =
                mensagemService.buscarMensagens(
                        mentoriaId,
                        usuario,
                        pageable
                );

        return ResponseEntity.ok(mensagens);
    }

    @PostMapping
    public ResponseEntity<MensagemResponse> enviarMensagem(
            @PathVariable Long mentoriaId,
            @Valid @RequestBody MensagemRequest request,
            @AuthenticationPrincipal User usuario
    ) {

        MensagemResponse mensagem =
                mensagemService.enviarMensagem(
                        mentoriaId,
                        usuario,
                        request
                );

        messagingTemplate.convertAndSend(
                "/topic/mentorias/" + mentoriaId + "/mensagens",
                mensagem
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }
}