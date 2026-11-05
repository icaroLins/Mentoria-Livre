package com.mentorialivre.mentoria_livre.dto;
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tipo,
        long expiraEmMs,
        UserResponse usuario
) {
}