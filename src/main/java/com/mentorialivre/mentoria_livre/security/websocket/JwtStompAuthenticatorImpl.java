
package com.mentorialivre.mentoria_livre.security.websocket;

import com.mentorialivre.mentoria_livre.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtStompAuthenticatorImpl implements JwtStompAuthenticator {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(String token) {

        // 1. Validar o JWT e recuperar o email
        String email = tokenService.validateAndGetSubject(token)
                .orElseThrow(() ->
                        new BadCredentialsException("Token inválido"));

        // 2. Recuperar o usuário
        UserDetails user = userDetailsService.loadUserByUsername(email);

        // 3. Construir Authentication
        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
    }
}
