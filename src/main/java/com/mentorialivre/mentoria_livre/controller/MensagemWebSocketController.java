package com.mentorialivre.mentoria_livre.controller;
import com.mentorialivre.mentoria_livre.dto.MensagemRequest;
import com.mentorialivre.mentoria_livre.dto.MensagemResponse;
import com.mentorialivre.mentoria_livre.model.user.User;
import com.mentorialivre.mentoria_livre.service.mensagens.MensagemService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class MensagemWebSocketController {

    private final MensagemService mensagemService;
    private final SimpMessagingTemplate messagingTemplate;

    public MensagemWebSocketController(
            MensagemService mensagemService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.mensagemService = mensagemService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/mentorias/{mentoriaId}/mensagens")
    public void enviarMensagem(
            @DestinationVariable Long mentoriaId,
            @Valid @Payload MensagemRequest request,
            Authentication authentication
    ) {

        User usuario = (User) authentication.getPrincipal();

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
    }
}
