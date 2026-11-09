package com.mentorialivre.mentoria_livre.security.websocket;

import org.springframework.security.core.Authentication;

public interface JwtStompAuthenticator {

    Authentication authenticate(String token);
}