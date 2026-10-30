package com.mentorialivre.mentoria_livre.dto;

public record AuthResponse(
        String token,
        String tipo,
        long expiraEmMs,
        UserResponse usuario
) {
}
