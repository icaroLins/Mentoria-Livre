package com.mentorialivre.mentoria_livre.security.websocket;

import com.mentorialivre.mentoria_livre.model.user.User;
import com.mentorialivre.mentoria_livre.service.mensagens.ChatPermissionService;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StompAuthenticationInterceptor
        implements ChannelInterceptor {

    private static final Pattern SEND_PATTERN =
            Pattern.compile("^/app/mentorias/(\\d+)/mensagens$");

    private static final Pattern SUBSCRIBE_PATTERN =
            Pattern.compile("^/topic/mentorias/(\\d+)/mensagens$");

    private final JwtStompAuthenticator jwtStompAuthenticator;
    private final ChatPermissionService chatPermissionService;

    public StompAuthenticationInterceptor(
            JwtStompAuthenticator jwtStompAuthenticator,
            ChatPermissionService chatPermissionService
    ) {
        this.jwtStompAuthenticator = jwtStompAuthenticator;
        this.chatPermissionService = chatPermissionService;
    }

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel
    ) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class
                );

        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {
            autenticarConexao(accessor);
            return message;
        }

        if (StompCommand.SEND.equals(command)) {
            validarEnvio(accessor);
            return message;
        }

        if (StompCommand.SUBSCRIBE.equals(command)) {
            validarInscricaoNoCanal(accessor);
            return message;
        }

        return message;
    }

    private void autenticarConexao(StompHeaderAccessor accessor) {

        String authorization =
                accessor.getFirstNativeHeader("Authorization");

        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

            throw new MessageDeliveryException(
                    "Token de autenticação não informado!"
            );
        }

        String token = authorization.substring(7);

        Authentication authentication =
                jwtStompAuthenticator.authenticate(token);

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new MessageDeliveryException(
                    "Não foi possível autenticar a conexão!"
            );
        }

        accessor.setUser(authentication);
    }

    private void validarEnvio(StompHeaderAccessor accessor) {

        String destination = accessor.getDestination();

        Matcher matcher = SEND_PATTERN.matcher(
                destination != null ? destination : ""
        );

        if (!matcher.matches()) {
            throw new AccessDeniedException(
                    "Destino de envio não permitido!"
            );
        }

        Long mentoriaId = Long.valueOf(matcher.group(1));

        User usuario = obterUsuarioAutenticado(accessor.getUser());

        chatPermissionService.validarEnvioDeMensagem(
                mentoriaId,
                usuario
        );
    }

    private void validarInscricaoNoCanal(StompHeaderAccessor accessor) {

        String destination = accessor.getDestination();

        Matcher matcher = SUBSCRIBE_PATTERN.matcher(
                destination != null ? destination : ""
        );

        if (!matcher.matches()) {
            throw new AccessDeniedException(
                    "Destino de inscrição não permitido!"
            );
        }

        Long mentoriaId = Long.valueOf(matcher.group(1));

        User usuario = obterUsuarioAutenticado(accessor.getUser());

        chatPermissionService.validarAcessoAoChat(
                mentoriaId,
                usuario
        );
    }

    private User obterUsuarioAutenticado(Principal principal) {

        if (!(principal instanceof Authentication authentication)) {
            throw new AccessDeniedException(
                    "Usuário não autenticado!"
            );
        }

        if (!(authentication.getPrincipal() instanceof User usuario)) {
            throw new AccessDeniedException(
                    "Principal inválido!"
            );
        }

        return usuario;
    }
}
