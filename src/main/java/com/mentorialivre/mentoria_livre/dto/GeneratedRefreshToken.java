package com.mentorialivre.mentoria_livre.dto;

import com.mentorialivre.mentoria_livre.model.refreshToken.RefreshToken;

public record GeneratedRefreshToken(
        String rawToken,
        RefreshToken entity
) {
}
